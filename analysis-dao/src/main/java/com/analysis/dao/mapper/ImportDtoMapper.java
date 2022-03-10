package com.analysis.dao.mapper;

import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mybatis.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 15:07
 */
@Mapper
public interface ImportDtoMapper extends SuperMapper<ImportDto> {

    Integer batchInsert(List<ImportDto> importData);

    List<ImportDto> query(ImportDto importDto);

    Integer selectId(ImportDto importDto);


}
