package com.analysis.service.service.impl.AnomalyDetectServiceImpl.ThreeSigma;

import com.analysis.service.service.AnomalyDetectService;
import com.analysis.service.utils.MathTool;
import com.analysis.service.utils.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/9 15:57
 */
public class ThreeSigmaImpl implements AnomalyDetectService {
    private ArrayList<Result> results;

    @Override
    public void timeSeriesAnalyse(double[] data) {
        judge(data);
        System.out.println("3-sigma运行完毕" );

    }

    @Override
    public List<Result> getResults() {
        return results;
    }


    public double[] residualError(double[] data) {//原始数组的剩余误差方法
        double[] rE = new double[]{};
        for (int x = 0; x < data.length; x++) {
            rE[x] = data[x] - MathTool.getAverageFromArray(data);
        }
        return rE;
    }


    public void judge(double[] data) {//判断异常值方法，若异常，则输出
        ArrayList<Result> res = new ArrayList<>();
        for (int x = 0; x < data.length; x++) {
            if (Math.abs(data[x] - MathTool.getAverageFromArray(data)) > (3 * MathTool.getStdDeviation(data))) {
                System.out.println("该数组中的索引为" + x + "的元素属于异常值");
                res.add(new Result(x,data[x]));
            }
        }
        setResults(res);
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

}
