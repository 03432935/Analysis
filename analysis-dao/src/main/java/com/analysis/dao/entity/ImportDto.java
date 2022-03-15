package com.analysis.dao.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Objects;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/20 20:25
 */
@Data
@EqualsAndHashCode
@TableName("import_data")
public class ImportDto extends Base<ImportDto> {

    @TableId
    private Long id;

    @ExcelProperty("SENID")
    private String senId;

    /**
     * 这里用string 去接日期才能格式化
     */
//    @DateTimeFormat("yyyy/mm/dd hh:mm")
    @ExcelProperty("TIME")
    private Date tTime;

    @ExcelProperty("V")
    private Double vData;

    @ExcelProperty("AVGV")
    private Double avgV;

    @ExcelProperty("MAXV")
    private Double maxV;

    @ExcelProperty("MINV")
    private Double minV;

    @ExcelProperty("MAXT")
    private Date maxTime;

    @ExcelProperty("MINT")
    private Date minTime;

    @ExcelProperty("S")
    private Double sData;

    @ExcelProperty("AVGS")
    private Double avgS;

    @ExcelProperty("MAXS")
    private Double maxS;

    @ExcelProperty("MINS")
    private Double minS;

    /**
     * 补全策略编号,原数据默认为0
     */
    private String completionStrategy = "0";

    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    private Date endTime;

    public String getCompletionStrategy(){
        return completionStrategy;
    }

    public void setCompletionStrategy(String completionStrategy){
        if(completionStrategy == null){
            completionStrategy = "0";
        }
        this.completionStrategy = completionStrategy;
    }

    public boolean isEmptyAll(ImportDto dto){
        boolean senFlag = false;
        boolean strategyFlag = false;
        if (dto.getSenId() == null || Objects.equals(dto.getSenId(), "")) {
            senFlag = true;
        }
        if (dto.getCompletionStrategy() == null || "".equals(dto.getCompletionStrategy())) {
            strategyFlag = true;
        }
        return senFlag && dto.getTTime() == null && dto.getVData() == null &&
                dto.getSData() == null && strategyFlag &&
                dto.getStartTime() == null && dto.getEndTime() == null
                ;
    }

    @Override
    public String toString() {
        return "ImportData{" +
                "senId='" + senId + '\'' +
                ", tTime=" + tTime +
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
