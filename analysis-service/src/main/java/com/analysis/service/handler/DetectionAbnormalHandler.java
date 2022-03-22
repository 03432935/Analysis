package com.analysis.service.handler;

import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.entity.ImportKeyDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.AbnormalDataEnum;
import com.analysis.service.enums.OperationEnum;
import com.analysis.service.factory.StrategyFactory;
import com.analysis.service.service.ConversionParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/16 17:52
 */
@Slf4j
public class DetectionAbnormalHandler extends AbstractStrategy<ImportDto> {

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private ConversionParamService conversionParamService;

    @Override
    protected List<ImportDto> before() {
        log.info("查询数据异常前置处理：---搜索全量的数据");
        List<ImportDto> list = importDtoMapper.selectList(null);
        log.info("全量导入的数据结果：{}",list);
        return list;
    }

    @Override
    protected List<ImportDto> analysisStrategy(List<ImportDto> importDtoList) throws Exception {
        //获取到重点数据senId(传感器编号),v(垂线),t(时间),并计算正常值范围
        List<ImportKeyDto> keyDtos = conversionParamService.importListToImportKey(importDtoList);

        //
        List<String> senIdList = keyDtos.stream().map(ImportKeyDto::getSenId).distinct().collect(Collectors.toList());
        for (String senId : senIdList){
            List<ImportKeyDto> dtoList = keyDtos.stream().filter(importKeyDto -> !importKeyDto.getSenId().equals(senId)).collect(Collectors.toList());
            //某一senId对应的数值
            Long count = dtoList.stream().map(ImportKeyDto::getVData).count();
            Double sumV = dtoList.stream().mapToDouble(ImportKeyDto::getVData).sum();
            Double maxV = dtoList.stream().mapToDouble(ImportKeyDto::getVData).max().orElse(0.0);
            Double minV = dtoList.stream().mapToDouble(ImportKeyDto::getVData).min().orElse(0.0);
            Double avgV = dtoList.stream().mapToDouble(ImportKeyDto::getVData).average().orElse(0.0);

            for(ImportKeyDto importKeyDto : dtoList){


            }
        }

        //日平均
        Double normalDayAvg = 0.0;
        Double normalDayMinAvg = 0.0;
        Double normalDayMaxAvg = 0.0;
        //周平均

        //月平均
        Double sum = 0.0;
        List<Double> vData = importDtoList.stream().map(ImportDto::getVData).collect(Collectors.toList());
        for (Double v: vData){
            sum = v+sum;
        }
        return null;
    }

    @Override
    protected void after(List<ImportDto> list) {

    }

    @Override
    protected void compensate() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(OperationEnum.ANOMALY_DETECTION.getName(),this);
    }

    private Double avgCalculation(List<ImportKeyDto> dtoList, Date time){
        String strTime = DateUtils.dateTimeToStrDay(time);
        return null;
    }
}
