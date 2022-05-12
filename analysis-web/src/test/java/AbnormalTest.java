import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.service.AbnormalJudgeService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD.GrubbsImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ExponentialMoving.HoltWintersImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.IsolationTree.IsolationTreeImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.LOF.LOFDetectImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ThreeSigma.ThreeSigmaImpl;
import com.analysis.service.utils.DisplayTool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geologic.hazard.analysis.web.StartApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
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
    private static final double ALPHA = 0.1;
    private static final double STEP = 0.01;
    private static final int TIMES = 1000;
    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private AbnormalJudgeService abnormalJudgeService;

    @Autowired
    private ConversionParamService conversionParamService;

    @Test
    public void threeSigmaTest(){
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("v_data");
        queryWrapper.eq("sen_id","1110000201");
        List<ImportDto> list = importDtoMapper.selectList(queryWrapper);

        List<ImportDto> res = abnormalJudgeService.threeSigmaFunction(list);
        System.out.println(res);
        //4393条数据里42条异常
        //4519条数据中有131条异常 其中126条是不同编号的那个

    }

    @Before
    public void setup(){
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("v_data");
        queryWrapper.eq("sen_id","1110002104");
        List<ImportDto> importDtoList = importDtoMapper.selectList(queryWrapper);
        data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
    }

    @Test
    public void threeSigmaATest(){
        ThreeSigmaImpl threeSigmaImpl = new ThreeSigmaImpl();
        threeSigmaImpl.timeSeriesAnalyse(data);
        DisplayTool.showResult(threeSigmaImpl);
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
        LOFDetectImpl lofDetect = new LOFDetectImpl(500, 10);
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
        IsolationTreeImpl isolationTree = new IsolationTreeImpl(180,256);
        isolationTree.timeSeriesAnalyse(data);
        DisplayTool.showResult(isolationTree);
    }
}
