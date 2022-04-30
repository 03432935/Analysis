package com.analysis.service.service;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.entity.ImportKeyDto;

import java.text.ParseException;
import java.util.Date;
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

    /**
     * 数据预处理的重采样函数
     */
    public List<EchartDto> resampling(List<ImportDto> list) throws Exception;

    /**
     * python传回的string变成List<AvgDto>
     */
    public List<AvgDto> pyBackStringToAvg(String string, Date date);
}
