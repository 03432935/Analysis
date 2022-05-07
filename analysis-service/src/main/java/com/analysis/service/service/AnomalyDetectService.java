package com.analysis.service.service;

import com.analysis.service.utils.Result;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/7 16:34
 */
public interface AnomalyDetectService {
    /**
     * 对时间序列进行异常检测
     */
    void timeSeriesAnalyse(double[] data);

    List<Result> getResults();
}
