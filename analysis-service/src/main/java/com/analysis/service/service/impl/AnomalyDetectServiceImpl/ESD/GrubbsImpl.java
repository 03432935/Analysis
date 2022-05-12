package com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD;

import com.analysis.service.service.AnomalyDetectService;
import com.analysis.service.utils.MathTool;
import com.analysis.service.utils.Result;


import java.util.ArrayList;

/**
 * @program: GrubbsTool
 * @description: Grubbs method to detect anomaly point
 * @author: mezereonxp Email: mezereonxp@gmail.com
 * @create: 2018-05-03 15:08
 **/
public class GrubbsImpl implements AnomalyDetectService {

    private double average;// 平均值
    private double stdDeviation;// 样本标准差
    private double[] G;// 可疑值
    private double G_MAX = 3.754;// 100个样本执行概率为99.50%的阈值(查表得知
    private ArrayList<Result> results;// 结果集

    public GrubbsImpl() {
    }

    public GrubbsImpl(double g) {
        G_MAX = g;
    }

    @Override
    public void timeSeriesAnalyse(double[] data) {
        results = new ArrayList<>();
        average = MathTool.getAverageFromArray(data);
        stdDeviation = MathTool.getStdDeviation(data);
        G = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            G[i] = Math.abs((data[i] - average) / stdDeviation);
            if (G[i] > G_MAX) {
                results.add(new Result(i, data[i]));
                System.out.println("Anomaly point! value is : " + data[i]);
            }
        }
    }

    @Override
    public ArrayList<Result> getResults() {
        return this.results;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getStdDeviation() {
        return stdDeviation;
    }

    public void setStdDeviation(double stdDeviation) {
        this.stdDeviation = stdDeviation;
    }
}
