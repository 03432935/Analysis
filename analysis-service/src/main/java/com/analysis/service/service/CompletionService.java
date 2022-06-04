package com.analysis.service.service;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/16 22:43
 */
public interface CompletionService {

    public List<EchartDto> strategyAvgFunction(ImportDto importDto) throws Exception;

    public List<EchartDto> holtWintersCompletionFunction(ImportDto importDto) throws Exception;

    public List<EchartDto> armaCompletionFunction(ImportDto importDto) throws Exception;

    public List<EchartDto> randomForestFunction(ImportDto importDto);
}
