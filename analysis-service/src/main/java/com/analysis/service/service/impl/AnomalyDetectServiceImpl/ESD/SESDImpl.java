package com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD;


import com.analysis.service.service.AnomalyDetectService;
import com.analysis.service.utils.Result;

import java.util.ArrayList;

/**
 * @program: AnomalyDetectTool
 * @description: Seasonal Extreme Studentized Deviate
 * @author: mezereonxp Email: mezereonxp@gmail.com
 * @create: 2018-05-07 18:16
 **/
public class SESDImpl implements AnomalyDetectService {

    private double t;// t统计量
    private ArrayList<Result> results;
    private ESDImpl esdImpl;

    /**
     * constructor
     */
    public SESDImpl() {
        esdImpl = new ESDImpl(1);
    }

    @Override
    public void timeSeriesAnalyse(double[] data) {
        esdImpl.timeSeriesAnalyse(data);
        results = esdImpl.getResults();
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

    public ESDImpl getEsdTool() {
        return esdImpl;
    }

    public void setEsdTool(ESDImpl esdImpl) {
        this.esdImpl = esdImpl;
    }
}
