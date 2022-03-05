package com.analysis.service.service;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportData;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 17:12
 */
public interface BatchQueryImportService extends SuperService<ImportData>{

    /**
     * 获取导入的分页数据
     * @param importData
     * @return
     */
    public IPage<ImportData> query(ImportData importData);

    /**
     * 获取导入的原始全部数据
     * @return
     */
    public List<ImportData> getList();

    /**
     * 获取导入的time和v数据提供给echart
     */
    public List<EchartDto> getEchartData(List<ImportData> importData);
}
