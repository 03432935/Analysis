package com.analysis.service.service;

import com.analysis.dao.entity.ImportDto;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 17:12
 */
public interface ImportExcelService {

    public void save(List<ImportDto> importDtoList);
}
