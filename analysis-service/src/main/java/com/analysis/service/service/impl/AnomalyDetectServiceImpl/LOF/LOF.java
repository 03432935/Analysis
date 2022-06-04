package com.analysis.service.service.impl.AnomalyDetectServiceImpl.LOF;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/14 13:20
 */
public class LOF {

    private int k;

    public LOF(int k) {
        this.k = k;
    }

    /**
     * 返回异常程度的分数， 越接近1则越异常
     *
     * @param knn 输入一个时序数据生成的旋转矩阵
     * @param x 输入测试的序列
     */
    public double getLOF(double[][] knn, double[] x) {
        double sum = 0;
        for (double[] o : knn) {
            sum += getLocDens(knn, o) / getLocDens(knn, x);
        }
        return sum / k;
    }


    /**
     * 获取local density
     *
     * @param knn 输入一个时序数据生成的旋转矩阵
     * @param x 输入测试的序列
     */
    public double getLocDens(double[][] knn, double[] x) {
        double[] nnk = findKthPoint(knn, x);

        double sum = 0;
        for (double[] o : knn) {
            sum += reachDist(o, x, nnk);
        }
        return sum / k;
    }


    /**
     * 找到第k个相似的序列
     *
     * @param knn 输入一个时序数据生成的旋转矩阵
     * @param x 输入测试的序列
     */
    public double[] findKthPoint(double[][] knn, double[] x) {

        ArrayList<double[]> list = new ArrayList<>(Arrays.asList(knn));
        int index = 0;
        double minDist;

        //k默认为1
        for (int i = 0; i < k; i++) {
            //记录最小的dist
            index = 0;
            minDist = dist(list.get(0), x);
            for (int j = 0; j < list.size(); j++) {
                if (minDist > dist( list.get(j), x)) {
                    minDist = dist( list.get(j), x);
                    index = j;
                }
            }
            if (i != k - 1) {
                list.remove(index);
            }
        }

        return list.get(index);
    }



    /**
     * 返回与相似序列的距离比较之下的较大值
     *
     * @param o 输入序列
     * @param x 测试序列
     * @param nnk 第k相似的序列
     */
    public double reachDist(double[] o, double[] x, double[] nnk) {
        return Math.max(dist(o, x), dist(nnk, x));
    }


    /**
     * 返回序列之间的欧几里德距离
     *
     * @param nnk 第k相似的序列
     * @param x 测试序列
     */
    private double dist(double[] nnk, double[] x) {

        double sum = 0;
        for (int i = 0; i < nnk.length; i++) {
            sum += (nnk[i] - x[i]) * (nnk[i] - x[i]);
        }

        return Math.sqrt(sum);
    }

//    /**
//     * 返回序列之间的欧几里德距离
//     *
//     * @param nnk 第k相似的序列
//     * @param x 测试序列
//     */
//    private double dist(double[] nnk, double x) {
//
//        double sum = 0;
//        for (int i = 0; i < nnk.length; i++) {
//            sum += (nnk[i] - x) * (nnk[i] - x);
//        }
//
//        return Math.sqrt(sum);
//    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}