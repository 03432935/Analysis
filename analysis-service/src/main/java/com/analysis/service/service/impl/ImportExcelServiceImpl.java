package com.analysis.service.service.impl;

import com.analysis.dao.entity.ImportData;
import com.analysis.dao.mapper.ImportDataMapper;
import com.analysis.service.service.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/20 21:04
 */
@Service
public class ImportExcelServiceImpl implements ImportExcelService {

    @Autowired
    private ImportDataMapper importDataMapper;

    @Override
    public void save(List<ImportData> importDataList){
        List<ImportData> list = new ArrayList<>();
        for (ImportData importData : importDataList){
            int id = importDataMapper.selectId(importData);
            if (id == 0){
                list.add(importData);
            }else{
                //todo:update?
            }
        }
        //这个list为空的可能
        importDataMapper.batchInsert(list);
    }
}
