package com.analysis.service.service;

import com.analysis.dao.entity.ImportData;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 17:12
 */
public interface BatchQueryImportService extends SuperService<ImportData>{

    public IPage<ImportData> query(ImportData importData);
}
