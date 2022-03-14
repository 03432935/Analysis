package com.analysis.service.service.impl;

import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.service.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private ImportDtoMapper importDtoMapper;

    @Override
    @Async
    public void save(List<ImportDto> importDtoList){
        //导入Excel进行异步操作
        List<ImportDto> list = new ArrayList<>();
        for (ImportDto importDto : importDtoList){
            Integer id = importDtoMapper.selectId(importDto);
            if (id == null){
                list.add(importDto);
            }else{
                //todo:update?
            }
        }
        //这个list为空的可能
        if(!list.isEmpty()){
            importDtoMapper.batchInsert(list);
        }
    }
}
