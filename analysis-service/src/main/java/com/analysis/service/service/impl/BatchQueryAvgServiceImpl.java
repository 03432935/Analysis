package com.analysis.service.service.impl;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.AvgDtoMapper;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.service.BatchQueryAvgService;
import com.analysis.service.service.BatchQueryImportService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 16:51
 */
@Slf4j
public class BatchQueryAvgServiceImpl extends SuperServiceImpl<AvgDtoMapper, AvgDto> implements BatchQueryAvgService {

    @Autowired
    private AvgDtoMapper avgDtoMapper;

    @Override
    public List<AvgDto> getAvgList(AvgDto dto) {
        List<AvgDto> avgDtos = avgDtoMapper.getAvgListByAvg(dto);
        log.info("BatchQueryAvgServiceImpl.getAvgList:in:{},out:{}",dto,avgDtos);
        return avgDtos;
    }

}
