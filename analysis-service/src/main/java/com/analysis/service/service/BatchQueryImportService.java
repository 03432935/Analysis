package com.analysis.service.service;

import com.analysis.dao.entity.ImportData;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 17:12
 */
public interface BatchQueryImportService {

    public PageInfo<ImportData> query(ImportData importData);
}
