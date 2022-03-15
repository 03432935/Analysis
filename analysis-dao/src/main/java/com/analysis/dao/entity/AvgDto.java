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
 * @date: 2022/3/10 16:31
 */
@Data
@TableName("handle_data")
public class AvgDto extends Base<AvgDto> {

    @TableId
    private Long id;

    private String senId;

    private Date tTime;

    private Double vData;

    private Double sData;

    private String strategyCode;

    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    private Date endTime;

    public boolean isEmptyAll(AvgDto dto){
        boolean senFlag = false;
        boolean strategyFlag = false;
        if (dto.getSenId() == null || Objects.equals(dto.getSenId(), "")) {
             senFlag = true;
        }
        if (dto.getStrategyCode() == null || "".equals(dto.getStrategyCode())) {
            strategyFlag = true;
        }
        return senFlag && dto.getTTime() == null && dto.getVData() == null &&
                dto.getSData() == null && strategyFlag &&
                dto.getStartTime() == null && dto.getEndTime() == null
                ;
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
