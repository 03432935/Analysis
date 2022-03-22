package com.analysis.service.service;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 17:21
 */
public interface StrategyService {

    //异常检测策略
    public void abnormalDetectionRun(String code) throws Exception;

    //补全策略
    public void completionStrategyRun(String code) throws Exception;

    //预测策略
    public void predictionStrategyRun(String code) throws Exception;
}
