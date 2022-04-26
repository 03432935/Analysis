package com.analysis.service.handler;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.AvgDtoMapper;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
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

    @Override
    protected List<ImportDto> before(StrategyContext strategyContext) {
        log.info("准备要处理预测所用的数据---");
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
//        queryWrapper.isNotNull("v_data");
        //todo:条件没写完
        List<ImportDto> importDtoList = importDtoMapper.selectList(queryWrapper);
        log.info("importDtoList"+importDtoList);
        return importDtoList;
    }

    @Override
    protected List<AvgDto> analysisStrategy(List<ImportDto> importDtoList,StrategyContext strategyContext) {
        log.info("对现有数据进行处理---");
        List<Date> ttime = importDtoList.stream().map(ImportDto::getTTime).collect(Collectors.toList());
        List<Double> vData = importDtoList.stream().map(ImportDto::getVData).collect(Collectors.toList());
        //todo：之前只测试了一项vdata，参数加上时间的还没测，算法内容主要在py地方，可以直接调用库，没写完
        try {
            String path = this.getClass().getClassLoader().getResource("adf.py").getPath().substring(1).replace("/","\\");
            String[] args = new String[] { "py", path , String.valueOf(ttime), String.valueOf(vData)};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void after(List<AvgDto> list,StrategyContext strategyContext) {
        log.info("预测的数据写在handler表---");

        //todo：优化项：后续在mapper下面写一个batchinsert较好
        //todo:疑问项：这个需不需要update？  存在一个问题，因为算法花费时间较长，是不是应该主动缓存一下。（不过优先是先实现了再说）
        for (AvgDto avgDto : list){
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
