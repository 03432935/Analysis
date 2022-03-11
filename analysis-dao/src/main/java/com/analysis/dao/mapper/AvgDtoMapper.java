package com.analysis.dao.mapper;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.mybatis.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 16:41
 */
@Mapper
public interface AvgDtoMapper extends SuperMapper<AvgDto> {

    public List<AvgDto> getAvgListByAvg(AvgDto dto);
}
