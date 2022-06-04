package com.analysis.service.service;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/15 16:41
 */
public interface AlgorithmService {

    public List<EchartDto> AlgorithmRes(String name , List<ImportDto> importDtoList) throws Exception;

    public List<EchartDto> AlgorithmCompletionRes(ImportDto importDto) throws Exception;
}
