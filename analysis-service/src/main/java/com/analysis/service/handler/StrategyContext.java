package com.analysis.service.handler;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/26 14:50
 */

@Data
public class StrategyContext {

    private List<Double> avgValue;

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
