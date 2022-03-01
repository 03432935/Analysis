package com.analysis.dao.mapper;

import com.analysis.dao.entity.ImportData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 15:07
 */
@Mapper
public interface ImportDataMapper {

    Integer batchInsert(List<ImportData> importData);

    List<ImportData> query(ImportData importData);

    Integer selectId(ImportData importData);
}
