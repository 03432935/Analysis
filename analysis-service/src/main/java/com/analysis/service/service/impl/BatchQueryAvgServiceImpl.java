package com.analysis.service.service.impl;

import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.AvgDtoMapper;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.service.BatchQueryAvgService;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.StrategyService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 16:51
 */
@Slf4j
@Service
public class BatchQueryAvgServiceImpl extends SuperServiceImpl<AvgDtoMapper, AvgDto> implements BatchQueryAvgService {

    @Autowired
    private AvgDtoMapper avgDtoMapper;

    @Autowired
    private StrategyService strategyService;
    @Override
    public List<AvgDto> getAvgList(AvgDto dto) throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dto.getStartTime());
        calendar.add(Calendar.DATE,7); //把日期往后增加一天,整数  往后推,负数往前移动
        Date endTime = calendar.getTime(); //这个时间就是日期往后推一天的结果
        dto.setEndTime(endTime);
        List<AvgDto> avgDtos = avgDtoMapper.getAvgListByAvg(dto);
        log.info("BatchQueryAvgServiceImpl.getAvgList:in:{},out:{}",dto,avgDtos);
        //如果数据库没有存有对应的预测数据，则调用预测策略进行预测,再调用一次查询
        if (avgDtos.isEmpty()){
            strategyService.predictionStrategyRun(dto.getStrategyCode(), dto);
            List<AvgDto> strategyRes = avgDtoMapper.getAvgListByAvg(dto);
            dto.setStrategyCode(PredictionStrategyEnum.ORIGINAL.getCode());
            List<AvgDto> originalRes = avgDtoMapper.getAvgListByAvg(dto);
            strategyRes.addAll(originalRes);
            return strategyRes;
        }
        dto.setStrategyCode(PredictionStrategyEnum.ORIGINAL.getCode());
        List<AvgDto> originalRes = avgDtoMapper.getAvgListByAvg(dto);
        avgDtos.addAll(originalRes);
        return avgDtos;
    }

}
