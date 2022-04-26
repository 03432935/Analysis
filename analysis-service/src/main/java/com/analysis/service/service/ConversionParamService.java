package com.analysis.service.service;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.entity.ImportKeyDto;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 17:08
 */
public interface ConversionParamService {

    public List<EchartDto> importListToEchartDto(List<ImportDto> list) throws Exception;

    public List<EchartDto> avgListToEchartDto(List<AvgDto> list) throws Exception;

    public List<ImportKeyDto> importListToImportKey(List<ImportDto> list) throws Exception;

}