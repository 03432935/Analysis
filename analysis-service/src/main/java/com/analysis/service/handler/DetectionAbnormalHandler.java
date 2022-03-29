package com.analysis.service.handler;

import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.entity.ImportKeyDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.AbnormalDataEnum;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.enums.OperationEnum;
import com.analysis.service.factory.StrategyFactory;
import com.analysis.service.service.ConversionParamService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/16 17:52
 */
@Slf4j
@Component
public class DetectionAbnormalHandler extends AbstractStrategy<ImportDto> {

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private ConversionParamService conversionParamService;

    @Override
    public List<ImportDto> before(StrategyContext strategyContext) {
        log.info("查询数据异常前置处理：---搜索全量的数据");
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("v_data");
        List<ImportDto> list = importDtoMapper.selectList(queryWrapper);
        log.info("全量导入的数据结果：{}", list);
        return list;
    }

    @Override
    public List<ImportDto> analysisStrategy(List<ImportDto> importDtoList, StrategyContext strategyContext) throws Exception {
        List<ImportDto> importDtos = new ArrayList<>();
        //差值（即和平均值差多少会被认为是异常
        Double differenceDay = 5.0;
        Double differenceMonth = 4.0;
        Double differenceYear = 3.0;

        List<String> senIdList = importDtoList.stream().map(ImportDto::getSenId).distinct().collect(Collectors.toList());
        List<Double> avgValueList = new ArrayList<>();

        for (String senId : senIdList) {
            List<ImportDto> dtoList = importDtoList.stream().filter(importDto -> importDto.getSenId().equals(senId)).collect(Collectors.toList());

            for (ImportDto importDto : dtoList) {
                //日均
                Double avgDay = dtoList.stream().filter(
                                importDto1 ->
                                        DateUtils.dateTimeToStrDay(importDto.getTTime()).equals(DateUtils.dateTimeToStrDay(importDto1.getTTime())))
                        .mapToDouble(ImportDto::getVData).average().orElse(0.0);
                //月均
                Double avgMonth = dtoList.stream().filter(
                        importDto1 ->
                                DateUtils.dateTimeToStrMonth(importDto.getTTime()).equals(DateUtils.dateTimeToStrMonth(importDto1.getTTime()))
                ).mapToDouble(ImportDto::getVData).average().orElse(0.0);
                //年均
                Double avgYear = dtoList.stream().filter(
                        importDto1 ->
                                DateUtils.dateTimeToStrYear(importDto.getTTime()).equals(DateUtils.dateTimeToStrYear(importDto1.getTTime()))
                ).mapToDouble(ImportDto::getVData).average().orElse(0.0);

                if ((Math.abs(importDto.getVData() - avgDay) > differenceDay)
                        || (Math.abs(importDto.getVData() - avgMonth) > differenceMonth) ||
                        (Math.abs(importDto.getVData() - avgYear) > differenceYear)) {
                    importDtos.add(importDto);
                    Double avgValue = dtoList.stream().mapToDouble(ImportDto::getVData).average().orElse(0.0);
                    avgValueList.add(avgValue);
                }
            }
        }
        strategyContext.setAvgValue(avgValueList);
        return importDtos;
    }

    @Override
    public void after(List<ImportDto> list, StrategyContext strategyContext) {
        if (list.size() == 0) {
            log.info("没有异常出现,list:" + list);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if(!Objects.equals(list.get(i).getCompletionStrategy(), CompletionStrategyEnum.ORIGINAL.getCode())){
                    //如果是补全的数据，就跳过
                    continue;
                }
                //异常标志更新
                UpdateWrapper<ImportDto> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("abnormal", AbnormalDataEnum.NORMAL.getCode());
                if (strategyContext.getAvgValue().get(i) < list.get(i).getVData()) {
                    updateWrapper.set("abnormal", AbnormalDataEnum.BIG_ABNORMAL.getCode());
                }else{
                    updateWrapper.set("abnormal",AbnormalDataEnum.SMALL_ABNORMAL.getCode());
                }
                updateWrapper.eq("sen_id", list.get(i).getSenId());
                updateWrapper.eq("v_data", list.get(i).getVData());
                updateWrapper.eq("t_time", list.get(i).getTTime());
                importDtoMapper.update(list.get(i), updateWrapper);
                log.info("异常标志更新:" + list.get(i));
            }
        }
    }

    @Override
    public void compensate() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(OperationEnum.ANOMALY_DETECTION.getName(), this);
    }

}
