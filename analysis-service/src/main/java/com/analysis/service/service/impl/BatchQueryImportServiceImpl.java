package com.analysis.service.service.impl;

import com.analysis.dao.entity.ImportData;
import com.analysis.dao.mapper.ImportDataMapper;
import com.analysis.service.service.BatchQueryImportService;
import com.github.pagehelper.PageHelper;
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

//        //也可以直接返回PageInfo，注意doSelectPageInfo方法和doSelectPage
//        PageInfo pageInfo = PageHelper.startPage(1, 10).doSelectPageInfo(new ISelect() {
//            @Override
//            public void doSelect() {
//                importDataMapper.query(importData);
//            }
//        });

        PageHelper.startPage(1,10);
        List<ImportData> list = importDataMapper.query(importData);
        PageInfo<ImportData> info = new PageInfo<>(list);
        log.info("query.info.result:{}",info);
        return info;

//        Page<ImportData> page = new Page<>(1,10);
//        IPage<ImportData> iPage = importDataMapper.query(page);
//        PageUtils pageUtils = new PageUtils(iPage);
//        System.out.println("pageutils:"+pageUtils);
//        return pageUtils;
    }
}
