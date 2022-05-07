package com.analysis.service.utils.ADF;

import com.analysis.common.utils.DiffUtils;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/4/22 17:55
 */
public class ADFCheck {

    public static double ADFCheck(double[] arr) {

        //difflist 差分数列
        List<Double> difflist = DiffUtils.diffArr(arr);

        //计算最大窗口大小 windowSize
        int windowSize = (int) Math.ceil(Math.pow(arr.length / 100.0, 1.0 / 4) * 12.0) + 1;
        //slidingWindowLists 最大滑动窗口
        List<List<Double>> maxslidingsindowLists = SlidingWindow.slidingWindow(difflist, windowSize);
        // 输出代替后的最大窗口数组
        double[][] maxreplacewindow = replace(arr, maxslidingsindowLists);
        //在第一列增加常数项
        double[][] maxfullRHS = add2(maxreplacewindow);


        //将差分的list转换为数组
        int length = maxfullRHS[0].length - 2;
        double[] d = new double[difflist.size() - length];
        for (int j = 0, i = length; i < difflist.size(); j++, i++) {
            d[j] = difflist.get(i);
        }

        //找到最佳的滑动窗口大小
        int bestWindowSize = SlidingWindow.bestwindow(d, maxfullRHS) + 1;
        System.out.println("bestWindowSize = " + bestWindowSize);

        //返回最佳窗口list
        List<List<Double>> bestslidingsindowLists = SlidingWindow.slidingWindow(difflist, bestWindowSize);

        // 输出代替后的最佳窗口数组
        double[][] bestreplacewindow = replace(arr, bestslidingsindowLists);

        //在最后一列增加常数项
        double[][] bestresult = add1(bestreplacewindow);

        //将差分的list转换为数组
        int length2 = bestresult[0].length - 2;
        double[] d2 = new double[difflist.size() - length2];
        for (int j = 0, i = length2; i < difflist.size(); j++, i++) {
            d2[j] = difflist.get(i);
        }
        Matrix matrixa = new Matrix(d2);
        Matrix matrixb = new Matrix(bestresult);

        OLS ols = new OLS();

        //计算beta
        Matrix tempbetas = ols.Regress(matrixb, matrixa, false);
        double beta = tempbetas.getValue(0, 0);
        System.out.println("beta = " + beta);

        //计算残差
        Matrix res = ols.getResiduals();
        double residual = 0;
        double[][] ressum = res.getData();
        for (int i = 0; i < ressum.length; i++) {
            for (int j = 0; j < ressum[0].length; j++) {
                residual = ressum[i][j] * ressum[i][j] + residual;
            }
        }
        System.out.println("residual = " + residual);

        //计算标准差
        Matrix sse = ols.getStandartErrorsOfParameters();
        double std = sse.getValue(0, 0);
        System.out.println("std = " + std);

        double tValue = beta / std;
        System.out.println("t_value = " + tValue);
        //todo:该计算方式还需要改进，对比python库的方法结果进行数值比对，确认方法是否编写正确。
        double p = calc(calMackinnonp(tValue));
        System.out.println("p = " + p);

        return p;
    }


    public static double[][] add1(double[][] replacewindow) {
        double[][] result = new double[replacewindow.length][replacewindow[0].length + 1];
        for (int i = 0; i < replacewindow.length; i++) {
            for (int j = 0; j < replacewindow[0].length; j++) {
                result[i][j] = replacewindow[i][j];
            }
            result[i][replacewindow[0].length] = 1;
        }
        return result;
    }

    public static double[][] add2(double[][] replacewindow) {
        double[][] result = new double[replacewindow.length][replacewindow[0].length + 1];
        for (int i = 0; i < replacewindow.length; i++) {
            for (int j = 0, k = 1; j < replacewindow[0].length; j++, k++) {
                result[i][k] = replacewindow[i][j];
            }
            result[i][0] = 1;
        }
        return result;
    }

    public static double[][] replace(double[] arr, List<List<Double>> lists) {
        double[][] arrs = new double[lists.size()][lists.get(0).size()];
        for (int i = 0; i < lists.size(); i++) {
            List<Double> temp = lists.get(i);
            for (int j = 0; j < temp.size(); j++) {
                arrs[i][j] = temp.get(j);
            }
        }

        for (int i = arrs.length - 1, j = arr.length - 1; i >= 0; i--, j--) {
            arrs[i][0] = arr[j - 1];
        }
        return arrs;
    }

    public static double calMackinnonp(double num) {
        if (num > 2.74) {
            return 1;
        }
        if (num < -18.83) {
            return 0;
        }

        if (num < -1.61) {
            return 0.038269 * Math.pow(num, 2) + 1.4412 * num + 2.1659;
        } else {
            return -0.010368 * Math.pow(num, 3) - 0.12745 * Math.pow(num, 2) + 0.93202 * num + 1.7339;
        }
    }

    public static double calc(double u) {
        double y = Math.abs(u);
        double y2 = y * y;
        double z = Math.exp(-0.5 * y2) * 0.398942280401432678;
        double p = 0;
        int k = 28;
        double s = -1;
        double fj = k;

        if (y > 3) {
            for (int i = 1; i <= k; i++) {
                p = fj / (y + p);
                fj = fj - 1.0;
            }
            p = z / (y + p);
        } else {
            for (int i = 1; i <= k; i++) {
                p = fj * y2 / (2.0 * fj + 1.0 + s * p);
                s = -s;
                fj = fj - 1.0;
            }
            p = 0.5 - z * y / (1 - p);
        }
        if (u > 0) {
            p = 1.0 - p;
        }
        return p;
    }

}
