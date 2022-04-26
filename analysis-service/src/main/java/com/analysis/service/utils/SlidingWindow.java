package com.analysis.service.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @description:滑动窗口
 * @author: lingwanxian
 * @date: 2022/4/22 16:59
 */
public class SlidingWindow {

    public static List<List<Double>> slidingWindow(List<Double> rawlist, int size) {
        List<List<Double>> list = new ArrayList<>();
        for (int i = 0; i < rawlist.size() - size + 1; i++) {
            List<Double> doubles = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                doubles.add((Double) rawlist.get(i+j));
            }
            Collections.reverse(doubles);
            list.add(doubles);
        }
        for (int i = 0; i < rawlist.size() - size + 1 ; i++) {
            List<Double> doubles = new ArrayList<>();
            doubles = list.get(i);
        }
        return list;
    }

    public static int bestwindow(double[] diff ,double[][] fullRHS){
        double[] residualarr = new double[fullRHS[0].length];
        int size = 1;
        for(int i = 1 ; i < fullRHS[0].length ; i++){
            double[][] window = new double[fullRHS.length][i + 1];
            for(int j = 0 ; j < fullRHS.length ; j++) {
                for (int k = 0; k < i + 1; k++) {
                    window[j][k] = fullRHS[j][k];
                }
            }
            Matrix matrixa = new Matrix(diff);
            Matrix matrixb = new Matrix(window);
            OLS ols = new OLS();
            //计算beta
            Matrix tempbetas = ols.Regress(matrixb, matrixa, false);
            double beta = tempbetas.getValue(1,0);
            Matrix res = ols.getResiduals();
            double residual = 0;
            double[][] ressum = res.getData();
            for(int k = 0 ; k < ressum.length ;k++) {
                for (int t = 0; t < ressum[0].length; t++) {
                    residual = ressum[k][t] * ressum[k][t] + residual;
                }
            }

            double num = 2 * window[0].length + window.length * Math.log(residual / window.length);
            residualarr[i] = num;

        }
        double bestWindow = residualarr[1];
        for(int i = 1 ; i < residualarr.length ; i++){
            if(Double.compare(bestWindow, residualarr[i]) > 0) {
                bestWindow = residualarr[i];
                size = i;
            }

        }

        return (size - 1);
    }

}
