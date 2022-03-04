package com.analysis.dao.mapper;

import com.analysis.dao.entity.ImportData;
import com.analysis.dao.mybatis.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 15:07
 */
@Mapper
public interface ImportDataMapper extends SuperMapper<ImportData> {

    Integer batchInsert(List<ImportData> importData);

    List<ImportData> query(ImportData importData);

    Integer selectId(ImportData importData);

//    IPage<ImportData> selectPage();
}
