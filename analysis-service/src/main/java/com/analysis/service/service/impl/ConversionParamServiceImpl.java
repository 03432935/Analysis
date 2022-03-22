package com.analysis.service.service.impl;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.entity.ImportKeyDto;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.service.BatchQueryAvgService;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 17:08
 */
@Slf4j
@Service
public class ConversionParamServiceImpl implements ConversionParamService {

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @Autowired
    private BatchQueryAvgService batchQueryAvgService;

    @Autowired
    private ConversionParamService conversionParamService;

    @Autowired
    private StrategyService strategyService;

    @Override
    public List<EchartDto> importListToEchartDto(List<ImportDto> list) throws Exception {
        try {
            List<EchartDto> echartDtos = list.stream()
                    .map(
                            importData -> {
                                EchartDto echartDto = new EchartDto();
                                echartDto.setTimeData(importData.getTTime());
                                echartDto.setVData(importData.getVData());
                                return echartDto;
                            }
                    ).sorted(Comparator.comparing(EchartDto::getTimeData))
                    .collect(Collectors.toList());
            log.info("getEchartData.echartDtos:" + echartDtos);
            return echartDtos;
        } catch (Exception e) {
            log.error("ImportDto转成EchartDto出错：ImportDto:{}", list);
            throw new Exception("ImportDto转成EchartDto出错：ImportDto:{}" + list, e);
        }
    }

    @Override
    public List<EchartDto> avgListToEchartDto(List<AvgDto> list) throws Exception {
        try {
            List<EchartDto> echartDtos = list.stream()
                    .map(
                            avgDto -> {
                                EchartDto echartDto = new EchartDto();
                                echartDto.setTimeData(avgDto.getTTime());
                                echartDto.setVData(avgDto.getVData());
                                return echartDto;
                            }
                    ).sorted(Comparator.comparing(EchartDto::getTimeData))
                    .collect(Collectors.toList());
            log.info("getEchartData.echartDtos:" + echartDtos);
            return echartDtos;
        } catch (Exception e) {
            log.error("AvgDto转成EchartDto出错：avgDto:{}", list);
            throw new Exception("AvgDto转成EchartDto出错：avgDto:{}" + list, e);
        }
    }

    @Override
    public List<ImportKeyDto> importListToImportKey(List<ImportDto> list) throws Exception {
        try {
            List<ImportKeyDto> keyDtos = list.stream()
                    .map(
                            importDto -> {
                                ImportKeyDto importKeyDto = new ImportKeyDto();
                                importKeyDto.setSenId(importDto.getSenId());
                                importKeyDto.setTimeData(importDto.getTTime());
                                importKeyDto.setVData(importDto.getVData());
                                return importKeyDto;
                            }
                    ).sorted(Comparator.comparing(ImportKeyDto::getTimeData))
                    .collect(Collectors.toList());
            log.info("getImportKeyDto.keyDtos:" + keyDtos);
            return keyDtos;
        } catch (Exception e) {
            log.error("ImportDto转成ImportKeyDto出错：ImportDto:{}", list);
            throw new Exception("ImportDto转成ImportKeyDto出错：ImportDto:{}" + list, e);
        }

    }
}
