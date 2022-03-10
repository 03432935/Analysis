package com.analysis.dao.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 16:31
 */
@Data
@TableName("todo")
public class AvgDto extends Base<AvgDto> {

    private Long id;

    private String senId;

    private Date tTime;

    private Double vData;

    private Double sData;

    private String strategyCode;

    public boolean isEmptyAll(AvgDto dto){
        return dto.getSenId().isBlank() && dto.getTTime() == null && dto.getVData() == null &&
                dto.getSData() == null && dto.getStrategyCode().isBlank();
    }
    @Override
    public String toString() {
        return "AvgDto{" +
                "id=" + id +
                ", senId='" + senId + '\'' +
                ", tTime=" + tTime +
                ", vData=" + vData +
                ", sData=" + sData +
                ", strategyCode='" + strategyCode + '\'' +
                '}';
    }
}
