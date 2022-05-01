package com.analysis.service.handler;

import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.AvgDtoMapper;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import com.analysis.service.factory.ThreadPoolFactory;
import com.analysis.service.service.ConversionParamService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/15 17:44
 */
@Slf4j
@Component
public class PredictionARMAHandler extends AbstractStrategy<AvgDto> {

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private AvgDtoMapper avgDtoMapper;

    @Autowired
    private ConversionParamService conversionParamService;


    @Override
    protected List<ImportDto> before(StrategyContext strategyContext) {
        log.info("准备要处理预测所用的数据---");
        //todo:条件没写完
        //todo：打个比方，用户需要预测date后一周的数据,所以需要这之前1年或者半年的数据（暂定半年）

        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        Date end = strategyContext.getTime();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(end);
        calendar.add(Calendar.MONTH,-6); //把日期往后增加一天,整数  往后推,负数往前移动
        Date start = calendar.getTime();

        queryWrapper.eq("sen_id", strategyContext.getSenId());

        //没有异常的数据
        queryWrapper.eq("abnormal", "0");
        //给定预测时间前半年的数据
        queryWrapper.between("t_time",start,end);
        List<ImportDto> importDtoList = importDtoMapper.selectList(queryWrapper);
        log.info("importDtoList"+importDtoList);
        return importDtoList;
    }

    @Override
    protected List<AvgDto> analysisStrategy(List<ImportDto> importDtoList,StrategyContext strategyContext) throws Exception {
        log.info("对现有数据进行处理---");
        //先对数据进行重采样
        List<EchartDto> resampleRes = conversionParamService.resampling(importDtoList);
//        List<Date> ttime = resampleRes.stream().map(EchartDto::getTimeData).collect(Collectors.toList());
        List<Double> vData = resampleRes.stream().map(EchartDto::getVData).collect(Collectors.toList());
        //todo：之前只测试了一项vdata，参数加上时间的还没测，算法内容主要在py地方，可以直接调用库，没写完

        StringBuilder res = new StringBuilder();
        try {
            String path = this.getClass().getClassLoader().getResource("python/predictTest.py").getPath().substring(1).replace("/", "\\");
//            path 的结果"/D:/xuexi/analysis/analysis/analysis-web/target/test-classes/adf.py" 需要删去前面的斜杠
            System.out.println(path);
            String[] args = new String[]{"py", path, String.valueOf(vData)};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件

            //原先的写法容易因为errorStream的流卡死缓存区，导致sout不出来，所以分别开了两个线程进行输出
            final InputStream inputStream = proc.getInputStream();

            final InputStream errorStream = proc.getErrorStream();

            //由原来直接new thread()显式创建线程改成使用线程池

            ThreadPoolFactory executor = ThreadPoolFactory.getThreadPool();

            List<Runnable> runnables = new ArrayList<>();


            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        String line1 = null;

                        while ((line1 = in.readLine()) != null) {
                            System.out.println(line1);
                            if ("Result".equals(line1)){
                                res.append("begin");
                            }
                            if (!res.isEmpty()){
                                res.append(line1);
                            }
                        }
                        System.out.println("result"+res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try{
                            in.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };


            Runnable runnable2 = new Runnable() {
                @Override
                public void run() {
                    BufferedReader error = new BufferedReader(new InputStreamReader(errorStream));
                    try {
                        String line2 = null;
                        while ((line2 = error.readLine()) != null) {
                            System.out.println(line2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try{
                            error.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };

            runnables.add(runnable1);
            runnables.add(runnable2);

            executor.execute(runnables);

            proc.waitFor();
            proc.destroy();
            System.out.println("print print");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        List<AvgDto> avgDtos = conversionParamService.pyBackStringToAvg(res.toString(),strategyContext.getTime());
        for (AvgDto dto: avgDtos){
            dto.setSenId(strategyContext.getSenId());
        }
        log.info("利用python脚本返回的预测结果为：{}",avgDtos);
        System.out.println(avgDtos);
        return avgDtos;
    }

    @Override
    protected void after(List<AvgDto> list,StrategyContext strategyContext) throws Exception {
        log.info("预测的数据写在handler表---");
        //顺便加上原始数据
        //todo：优化项：后续在mapper下面写一个batchinsert较好
        //todo:疑问项：这个需不需要update？  存在一个问题，因为算法花费时间较长，是不是应该主动缓存一下。（不过优先是先实现了再说）
        for (AvgDto avgDto : list){
            //插入预测的数据
            avgDtoMapper.insert(avgDto);
        }
        //从数据库查询对应的原始数据
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        Date start = Collections.min(list.stream().map(AvgDto::getTTime).collect(Collectors.toList()));
        Date endTemp = Collections.max(list.stream().map(AvgDto::getTTime).collect(Collectors.toList()));
        String string = DateUtils.getOneDayEndTime(endTemp);
        Date end = DateUtils.strToDateTime(string);

        queryWrapper.between("t_time",start,end);
        List<ImportDto> importDtoList = importDtoMapper.selectList(queryWrapper);
        //对数据进行重采样
        List<EchartDto> echartDtos = conversionParamService.resampling(importDtoList);
        //然后插入到表里
        for (EchartDto echartDto:echartDtos){
            AvgDto avgDto = new AvgDto();
            avgDto.setSenId(strategyContext.getSenId());
            avgDto.setTTime(echartDto.getTimeData());
            avgDto.setVData(echartDto.getVData());
            avgDto.setStrategyCode(PredictionStrategyEnum.ORIGINAL.getCode());
            avgDtoMapper.insert(avgDto);
        }
    }

    @Override
    protected void compensate() {
        log.info("补偿措施---");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(PredictionStrategyEnum.ARMA.getName(), this);
    }


}
