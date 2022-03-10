package com.analysis.service.service;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 17:12
 */
public interface BatchQueryImportService extends SuperService<ImportDto>{

    /**
     * 获取导入的分页数据
     * @param importDto
     * @return
     */
    public IPage<ImportDto> query(ImportDto importDto);

    /**
     * 获取导入的原始全部数据
     * @return
     */
    public List<ImportDto> getList();

    /**
     * 获取条件满足下的数据
     */
    public List<ImportDto> getSomeList(ImportDto dto);

    /**
     * 获取导入的time和v数据提供给echart
     */
    public List<EchartDto> getEchartData(List<ImportDto> importData);
}
