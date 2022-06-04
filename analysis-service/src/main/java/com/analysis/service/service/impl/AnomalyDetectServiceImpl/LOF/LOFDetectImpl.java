package com.analysis.service.service.impl.AnomalyDetectServiceImpl.LOF;

import com.analysis.service.service.AnomalyDetectService;
import com.analysis.service.utils.MatrixUtil;
import com.analysis.service.utils.Result;

import java.util.ArrayList;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/14 13:59
 */
public class LOFDetectImpl implements AnomalyDetectService {

    private int T;// 时间序列用来训练的长度
    private int L;// 时间序列的所利用的窗口长度
    private int K = 1;//  LOF算法中的k值, 默认设置为1, 也就是取历史最相似的序列进行预测
    private ArrayList<Result> results;

    static final double SCORE = 0.9;

    /**
     * LOF检测工具的构造方法
     *
     * @param T 时间序列用来训练的长度
     * @param L 时间序列的所利用的窗口长度
     */
    public LOFDetectImpl(int T, int L) {
        this.T = T;
        this.L = L;
    }

    /**
     * 利用LOF进行时间序列分析
     * 打印窗口的异常分数, 越接近1则越异常
     */
    @Override
    public void timeSeriesAnalyse(double[] series) {
        System.out.println("analyse begin");
        // 利用T和L, 以及时间序列生成测试矩阵
        //10x482
        double[][] mat = MatrixUtil.getMat(series, T, series.length - T - L + 1, L);

        //一个窗口大小的测试序列, 默认是原序列中最后窗口大小的序列
//        double[] test = MatrixUtil.getTestSeries(series, series.length - L, L);

        //382x10,旋转矩阵  测试
        double[][] matC = MatrixUtil.getMatC(mat, T, series.length - T - L + 1, L);

        //100x10  训练
        double[][] matT = MatrixUtil.getMatT(mat, T, series.length - T - L + 1, L);

        LOF lof = new LOF(K);

        double[] ncmForC = new double[matC.length];

        //382
        for (int i = 0; i < matC.length; i++) {
            ncmForC[i] = lof.getLOF(matT, matC[i]);
        }


        int n = 0;
        ArrayList<Result> res = new ArrayList<>();
        while ((T + n * L) < series.length) {
            double count = 0;
            //如果不能整除
            double[] t;
            double ncmForT;
            if ((series.length - (T + n * L)) < L) {
                t = MatrixUtil.getTestSeries(series, T + n * L, series.length - (T + n * L));
                double[][] matW = MatrixUtil.getMatT(mat, T, series.length - T - L + 1, series.length - (T + n * L));
                ncmForT = lof.getLOF(matW,t);
            } else {
                t = MatrixUtil.getTestSeries(series, T + n * L, L);
                ncmForT = lof.getLOF(matT, t);
            }
            for (double x : ncmForC) {
                if (ncmForT <= x) {
                    count++;
                }
            }
            count /= matC.length;
            System.out.println("value is " + series[T + n * L] + " ,Anomaly Score is " + count);
            if (count > SCORE) {
                int temp = T + (n+1) * L;
                if ((series.length - (T + n * L)) < L){
                    temp = series.length;
                }
                for (int i = T + n * L; i < temp; i++) {
                    res.add(new Result(i, series[i]));
                }

            }
            n = n + 1;
        }
        setResults(res);
    }

    public int getT() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    public int getL() {
        return L;
    }

    public void setL(int l) {
        L = l;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    @Override
    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }
}
