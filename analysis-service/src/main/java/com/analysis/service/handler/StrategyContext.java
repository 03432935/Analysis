package com.analysis.service.handler;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/26 14:50
 */

@Data
public class StrategyContext {

    private List<Double> avgValue;

    /**
     * 比如用户给预测时间，给我预测某天一周的数据
     */
    private Date time;

    /**
     * 预测需要确定senid
     */
    private String senId;
//    /**
//     * 趋势成分
//     */
//    private boolean trend = false;
//
//    /**
//     * 季节成分
//     */
//    private boolean season = false;
}
