package com.analysis.service.service.impl.AnomalyDetectServiceImpl.ARIMA;

import com.analysis.service.service.AnomalyDetectService;
import com.analysis.service.utils.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/16 0:13
 */
public class ARIMAImpl implements AnomalyDetectService {

    private ArrayList<Result> results;

    private ArrayList<Double> predictData;

    private static final double DIFFVALUE = 3.0;

    @Override
    public void timeSeriesAnalyse(double[] data) {
        ArrayList<Result> resultArrayList = new ArrayList<>();
        ArrayList<Double> predictDataList = new ArrayList<>();
        int j = 1;
        //预测第i个数据
        for (int i = 1; i < data.length; i++) {
            double[] temp;
            if (data[i] == 0.0){
                j = Math.min(i,j);
            }else{
                j = i;
            }
            if (i > 100) {
                temp = Arrays.copyOfRange(data, i - 100, j);
            } else {
                temp = Arrays.copyOfRange(data, 0, j+1);
            }

            //进行预测
            double res = predictNextDay(temp);
            //预测后对比差值
            double diff = Math.abs(res - data[i]);
            //如果差值较大
            if (diff > DIFFVALUE) {
                System.out.println("predictData:" + res + ",originalData:" + data[i]);
                resultArrayList.add(new Result(i, data[i]));
                if (data[i] == 0.0){
                    predictDataList.add(res);
                }
            }
            setResults(resultArrayList);
            setPredictData(predictDataList);
        }

    }

    private double predictNextDay(double[] data) {
        ARIMAModel arima = new ARIMAModel(data);
        ArrayList<double[]> list = new ArrayList<>();
        int period = 7;
        int modelCnt = 5, cnt = 0; // 通过多次预测的平均值作为预测值
        double[] tmpPredict = new double[modelCnt];
        for (int k = 0; k < modelCnt; ++k) // 控制通过多少组参数进行计算最终的结果
        {
            System.out.println("==========");
            double[] bestModel = arima.getARIMAModel(period, list, k != 0);
            if (bestModel.length == 0) {
                tmpPredict[k] = data[data.length - period];
                cnt++;
                break;
            } else {
                double predictDiff = arima.predictValue(bestModel[0], bestModel[1], period);
                tmpPredict[k] = arima.aftDeal(predictDiff, period);
                cnt++;
            }
            System.out.println("BestModel is " + bestModel[0] + " " + bestModel[1]);
            list.add(bestModel);
        }
        double sumPredict = 0.0;
        for (int k = 0; k < cnt; ++k) {
            sumPredict += tmpPredict[k] / (double) cnt;
        }
//        不需要四舍五入
        double predict = sumPredict;
        System.out.println("Predict value=" + predict);
        return predict;
    }

    @Override
    public List<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public List<Double> getPredictData() {
        return predictData;
    }

    public void setPredictData(ArrayList<Double> predictData) {
        this.predictData = predictData;
    }

}
