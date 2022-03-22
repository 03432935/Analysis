package com.analysis.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/17 16:21
 */
@Data
public class ImportKeyDto {
    private String senId;
    private Date timeData;
    private Double vData;
}
