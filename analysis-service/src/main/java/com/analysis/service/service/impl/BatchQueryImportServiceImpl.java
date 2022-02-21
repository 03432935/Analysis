package com.analysis.service.service.impl;

import com.analysis.common.utils.PageUtils;
import com.analysis.dao.entity.ImportData;
import com.analysis.dao.mapper.ImportDataMapper;
import com.analysis.service.service.BatchQueryImportService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 16:12
 */
@Service
@Slf4j
public class BatchQueryImportServiceImpl implements BatchQueryImportService {

    @Autowired
    private ImportDataMapper importDataMapper;

    @Override
    public PageInfo<ImportData> query(ImportData importData){
        List<ImportData> list = importDataMapper.query(importData);
        return PageUtils.pageList(list,1,10);
    }
}
