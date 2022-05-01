package com.analysis.service.service;

import com.analysis.dao.entity.AvgDto;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 16:49
 */
public interface BatchQueryAvgService extends SuperService<AvgDto>{

    public List<AvgDto> getAvgList(AvgDto dto) throws Exception;

}
