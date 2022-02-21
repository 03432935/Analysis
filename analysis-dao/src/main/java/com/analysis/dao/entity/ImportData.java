package com.analysis.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/20 20:25
 */
@Data
@EqualsAndHashCode
public class ImportData {

//    @ExcelProperty(index = 0,value = "ID")
    private String senId;

//    @ExcelProperty(index = 1,value = "时间")
    private Date tTime;

//    @ExcelProperty(index = 2,value = "V数据")
    private Double vData;

//    @ExcelProperty(index = 3,value = "V平均值")
    private Double avgV;

//    @ExcelProperty(index = 4,value = "V最大值")
    private Double maxV;

//    @ExcelProperty(index = 5,value = "V最小值")
    private Double minV;

//    @ExcelProperty(index = 6,value = "最大时间")
    private Date maxTime;

//    @ExcelProperty(index = 7,value = "最小时间")
    private Date minTime;

//    @ExcelProperty(index = 8,value = "S数据")
    private Double sData;

//    @ExcelProperty(index = 9,value = "S平均值")
    private Double avgS;

//    @ExcelProperty(index = 10,value = "S最大值")
    private Double maxS;

//    @ExcelProperty(index = 11,value = "S最小值")
    private Double minS;

    @Override
    public String toString() {
        return "ImportData{" +
                "senId='" + senId + '\'' +
                ", time=" + tTime +
                ", vData=" + vData +
                ", avgV=" + avgV +
                ", maxV=" + maxV +
                ", minV=" + minV +
                ", maxTime=" + maxTime +
                ", minTime=" + minTime +
                ", sData=" + sData +
                ", avgS=" + avgS +
                ", maxS=" + maxS +
                ", minS=" + minS +
                '}';
    }
}
