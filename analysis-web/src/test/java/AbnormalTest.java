import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.service.AbnormalJudgeService;
import com.analysis.service.service.AlgorithmService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ARIMA.ARIMAImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD.GrubbsImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ExponentialMoving.HoltWintersImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.IsolationTree.IsolationTreeImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.LOF.LOFDetectImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ThreeSigma.ThreeSigmaImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geologic.hazard.analysis.web.StartApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/7 14:52
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
public class AbnormalTest {

    public double[] data;
    public List<ImportDto> importDtoList;

    private static final double ALPHA = 0.1;
    private static final double STEP = 0.01;
    private static final int TIMES = 1000;
    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private AbnormalJudgeService abnormalJudgeService;

    @Autowired
    private ConversionParamService conversionParamService;

    @Autowired
    private AlgorithmService algorithmService;
//    @Test
//    public void threeSigmaTest(){
//        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
//        queryWrapper.isNotNull("v_data");
//        queryWrapper.eq("sen_id","1110000201");
//        List<ImportDto> list = importDtoMapper.selectList(queryWrapper);
//
//        List<ImportDto> res = abnormalJudgeService.threeSigmaFunction(list);
//        System.out.println(res);
//        //4393条数据里42条异常
//        //4519条数据中有131条异常 其中126条是不同编号的那个
//
//    }

    @Before
    public void setup(){
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        String yearStart = "2016-06-02 00:00:00";
        String yearEnd = "2020-06-02 00:00:00";
        Date start = DateUtils.strToDateTime(yearStart);
        Date end = DateUtils.strToDateTime(yearEnd);
//1110002605  1110002602    1110030806
        queryWrapper.eq("sen_id","1110002602");
        queryWrapper.isNotNull("v_data");
        queryWrapper.eq("completion_strategy","0");
        queryWrapper.eq("del","0");
        queryWrapper.between("t_time", start, end);
        queryWrapper.eq("abnormal", "0");
        queryWrapper.orderByAsc("t_time");
        importDtoList = importDtoMapper.selectList(queryWrapper);
        data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
    }

    @Test
    public void threeSigmaATest() throws Exception {
        ThreeSigmaImpl threeSigmaImpl = new ThreeSigmaImpl();
        threeSigmaImpl.timeSeriesAnalyse(data);
        DisplayTool.showResult(threeSigmaImpl);
//        List<Result> res = threeSigmaImpl.getResults();
//        for (Result result: res){
//            ImportDto importDto = importDtoList.get(result.getIndex());
//            importDto.setAbnormal("1");
//            importDtoList.set(result.getIndex(), importDto);
//        }
//        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(importDtoList);
//        System.out.println(echartDtos);
    }

    @Test
    public void GrubbsTest(){
        System.out.println("grubbs begin ---------------");
        //没填参数时默认99.5%的阈值g
        GrubbsImpl grubbs = new GrubbsImpl();
        grubbs.timeSeriesAnalyse(data);
        DisplayTool.showResult(grubbs);
    }

    @Test
    public void LOFTest(){
        //todo：数组越界，要注意看总数和L的值，后续加个判断条件
        //滑动窗口，目前只不重叠的每L个作为一个窗口，
        // 具体的异常点没得出来，只得出异常点所在的窗口，以前500条作为训练样本，得出剩余数据中存在380条异常点
        //后续需要进行权重占比评分，得出具体的异常点，或者说LOF只作为异常点辅助参考
        LOFDetectImpl lofDetect = new LOFDetectImpl(100, 10);
        lofDetect.timeSeriesAnalyse(data);
        DisplayTool.showResult(lofDetect);
    }

    @Test
    public void holtWintersTest(){
        //算法过程中：计算时超过了double的精度范围e308
        HoltWintersImpl holtWinters = new HoltWintersImpl(ALPHA,STEP,TIMES);
        holtWinters.timeSeriesAnalyse(data);
        DisplayTool.showResult(holtWinters);
    }

    @Test
    public void isolationTreeTest(){
        //孤立森林算法：总点数4390，异常点数300~400
        IsolationTreeImpl isolationTree = new IsolationTreeImpl(100,256);
        isolationTree.timeSeriesAnalyse(data);
        DisplayTool.showResult(isolationTree);
    }

    @Test
    public void ARIMAImplTest(){
        ARIMAImpl arima = new ARIMAImpl();
        arima.timeSeriesAnalyse(data);
        DisplayTool.showResult(arima);
    }

    @Test
    public void voting(){
        List<Result> res = new ArrayList<>();

        System.out.println("three-sigma begin------------");
        ThreeSigmaImpl threeSigmaImpl = new ThreeSigmaImpl();
        threeSigmaImpl.timeSeriesAnalyse(data);
        List<Result> threeResult = threeSigmaImpl.getResults();
        res.addAll(threeResult);

        System.out.println("grubbs begin ---------------");
        //没填参数时默认99.5%的阈值g
        GrubbsImpl grubbs = new GrubbsImpl();
        grubbs.timeSeriesAnalyse(data);
        List<Result> grubbsResult = grubbs.getResults();
        res.addAll(grubbsResult);

        System.out.println("lof begin------------");
        LOFDetectImpl lofDetect = new LOFDetectImpl(300, 10);
        lofDetect.timeSeriesAnalyse(data);
        List<Result> lofDetectResult = lofDetect.getResults();
        res.addAll(lofDetectResult);

        System.out.println("iForest begin------------");
        IsolationTreeImpl isolationTree = new IsolationTreeImpl();
        isolationTree.timeSeriesAnalyse(data);
        List<Result> iForestResult = isolationTree.getResults();
        res.addAll(iForestResult);

        System.out.println("holtWinters begin------------");
        HoltWintersImpl holtWinters = new HoltWintersImpl(ALPHA,STEP,TIMES);
        holtWinters.timeSeriesAnalyse(data);

        List<Result> holtWintersResult = holtWinters.getResults();
        res.addAll(holtWintersResult);

        System.out.println("arima begin------------");
        ARIMAImpl arima = new ARIMAImpl();
        arima.timeSeriesAnalyse(data);
        List<Result> arimaResult = arima.getResults();
        res.addAll(arimaResult);

        Map<Integer, Integer> map = new HashMap<>();
        List<Result> resultList = new ArrayList<>();
        for(Result result : res){

            Integer count = map.get(result.getIndex());
            map.put(result.getIndex(), (count == null) ? 1 : count + 1);
//            int count = Collections.frequency(res,result);
            System.out.println("result:"+result+",count:"+count);

            if(count!=null && map.get(result.getIndex())==3){
                System.out.println("voting result:"+result);
                resultList.add(result);
            }

        }

        System.out.println("map:"+map);
        resultList.stream().distinct().collect(Collectors.toList());
        System.out.println(resultList);
        System.out.println(resultList.size());
    }

    @Test
    public void mvote() throws Exception {
        algorithmService.AlgorithmRes("7",importDtoList);

    }
}
