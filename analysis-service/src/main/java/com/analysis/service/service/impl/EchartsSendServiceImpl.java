package com.analysis.service.service.impl;

import com.analysis.common.utils.ResultUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.service.BatchQueryAvgService;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.EchartsSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class EchartsSendServiceImpl implements EchartsSendService {

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @Autowired
    private BatchQueryAvgService batchQueryAvgService;

    @Autowired
    private EchartsSendService echartsSendService;

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
    public List<EchartDto> judgeInput(AvgDto avgDto) throws Exception {
        if (avgDto == null || avgDto.getStrategyCode() == null || avgDto.getStrategyCode().isBlank()) {
            //如果策略为空,查询原始数据
            List<ImportDto> list;
            if (avgDto == null || avgDto.isEmptyAll(avgDto)) {
                //如果入参为空，则查询全部原数据
                list = batchQueryImportService.getList();
                log.info("EchartsController.echartsSendData.list(avgDto.isEmpty):" + list);
            } else {
                ImportDto dto = new ImportDto();
                if (avgDto.getSenId() != null && !avgDto.getSenId().isBlank()) {
                    dto.setSenId(avgDto.getSenId());
                }
                if (avgDto.getTTime() != null) {
                    dto.setTTime(avgDto.getTTime());
                }
                if (avgDto.getVData() != null) {
                    dto.setVData(avgDto.getVData());
                }
                if (avgDto.getSData() != null) {
                    dto.setSData(avgDto.getSData());
                }
                if (avgDto.getStartTime() != null) {
                    dto.setStartTime(avgDto.getStartTime());
                }
                if (avgDto.getEndTime() != null) {
                    dto.setEndTime(avgDto.getEndTime());
                }
                list = batchQueryImportService.getSomeList(dto);
                log.info("EchartsController.echartsSendData.list(avgDto.isNotEmpty):" + list);
            }
            List<EchartDto> echartDtos = echartsSendService.importListToEchartDto(list);
            log.info("EchartsController.echartsSendData.echartDtos" + echartDtos);
            return echartDtos;
        } else {
            //如果策略不为空，查询另一张表（处理了数据后的表）
            List<AvgDto> list = batchQueryAvgService.getAvgList(avgDto);
            log.info("EchartsController.echartsSendData.list(策略不为空):" + list);
            List<EchartDto> echartDtos = echartsSendService.avgListToEchartDto(list);
            log.info("EchartsController.echartsSendData.echartDtos" + echartDtos);
            return echartDtos;
        }
    }
}
