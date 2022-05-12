package com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD;

import com.analysis.service.service.AnomalyDetectService;
import com.analysis.service.utils.MathTool;
import com.analysis.service.utils.Result;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * @program: AnomalyDetectTool
 * @description: Extreme Studentized Deviate
 * @author: mezereonxp Email: mezereonxp@gmail.com
 * @create: 2018-04-28 11:41
 **/
public class ESDImpl implements AnomalyDetectService {

    private double average;// 平均值
    private double stdDeviation;// 方差
    private int K;// 第k个值(k个异常值
    private double t;// t统计量
    private double[] g;// Grubbs统计量
    private ArrayList<Result> results;

    public ESDImpl(int k) {
        this.K = k;
        this.g = new double[k];
    }

    @Override
    public void timeSeriesAnalyse(double[] data) {

        this.average = MathTool.getAverageFromArray(data);
        this.stdDeviation = MathTool.getStdDeviation(data);
        int kInputValue = K;
        for (K = 1; K <= data.length; K++) {
            ArrayList<Result> tempResults = new ArrayList<>();
            //计算与均值偏离最远的残差，ps：排除上一轮最大残差样本数据
            double kValue = this.getKExtremeValue(data, K);
            int n = data.length;
            int count = 0;
            for (int i = 0; i < n; i++) {
                t = Math.abs(data[i] - average) / (stdDeviation / Math.sqrt(n - K));
                //lambda为临界值
                double lambda = (n - K) * t / (Math.sqrt((n - K - 1 + t * t) * (n - K + 1)));
//                统计量大于临界值，说明为异常点
                if (lambda < kValue) {
                    tempResults.add(new Result(i, data[i]));
                    count++;
                }
                if (i == 44 || i == 74 || i == 104 || i == 352) {
                    System.out.println("i:" + i + ",lambda临界值:" + lambda);
                    System.out.println("kValue:"+kValue);
                }
            }
            System.out.println("k-value:" + kValue + ",count:" + count + ",K:" + K);
//            当异常点的数量为K个最大值点的数量时
            if (count == K) {
                System.out.println("满足相等");
                results = tempResults;
                break;
            } else {
//                System.out.println("不满足相等");
                results = tempResults;
                System.out.println(tempResults);
            }
        }

    }


    public double getKExtremeValue(double[] data, int k) {
        double[] temp = data.clone();
        for (int i = 0; i < data.length; i++) {
            temp[i] = Math.abs(temp[i] - average) / stdDeviation;
        }
        //取前k+1个最大值(倒序
        for (int i = 0; i < k + 1; i++) {
            for (int j = i + 1; j < data.length; j++) {
                if (temp[i] < temp[j]) {
                    double c = temp[i];
                    temp[i] = temp[j];
                    temp[j] = c;
                }
            }
        }
        return temp[k - 1];
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

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    @Override
    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public double[] getG() {
        return g;
    }

    public void setG(double[] g) {
        this.g = g;
    }
}
