package com.analysis.dao.mapper;

import com.analysis.dao.entity.ImportData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 15:07
 */
@Mapper
public interface ImportDataMapper {

    int batchInsert(List<ImportData> importData);

    List<ImportData> query(ImportData importData);

    int selectId(ImportData importData);
}
