package com.analysis.service.service.impl;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.EchartsSendService;
import com.analysis.service.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/17 16:28
 */
@Slf4j
@Service
public class EchartsSendServiceImpl implements EchartsSendService {

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private ConversionParamService conversionParamService;

    @Override
    public List<EchartDto> judgeInput(ImportDto importDto) throws Exception {
        List<ImportDto> list = null;
        //如果策略为空,查询原始数据
        if (importDto == null || importDto.isEmptyAll(importDto)) {
            //如果入参为空，则查询全部原数据
            list = batchQueryImportService.getList(importDto.getIsShowException());
            log.info("EchartsController.echartsSendData.list(importDto.isEmpty):" + list);
        } else {
            if (!importDto.getCompletionStrategy().equals(CompletionStrategyEnum.ORIGINAL.getCode())) {
                //如果补全策略不为默认，对数据进行处理,update
                strategyService.completionStrategyRun(importDto.getCompletionStrategy());
            }//如果没有策略不补数据，字段默认为0
            list = batchQueryImportService.getSomeList(importDto);
            log.info("EchartsController.echartsSendData.list(importDto.isNotEmpty):" + list);
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(list);
        log.info("EchartsController.echartsSendData.echartDtos" + echartDtos);
        return echartDtos;
    }

    @Override
    public List<ImportDto> beforeAlgorithm(ImportDto importDto) {
        return batchQueryImportService.getSomeList(importDto);
    }
}
