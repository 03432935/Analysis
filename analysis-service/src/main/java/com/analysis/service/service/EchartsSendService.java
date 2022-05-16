package com.analysis.service.service;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/17 16:27
 */
public interface EchartsSendService {

    public List<EchartDto> judgeInput(ImportDto importDto) throws Exception;

    public List<ImportDto> beforeAlgorithm(ImportDto importDto);
}
