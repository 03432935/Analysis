package com.analysis.service.utils;

import com.analysis.service.service.AnomalyDetectService;

import java.util.List;

/**
 * @program: AnomalyDetectTool
 * @description: A tool to display
 * @author: mezereonxp Email: mezereonxp@gmail.com
 * @create: 2018-05-08 14:43
 **/
public class DisplayTool {
    public static void showResult(AnomalyDetectService detectTool) {
        List<Result> res = detectTool.getResults();
        for (Result result : res) {
            System.out.println(result.toString());
        }
        System.out.println("总条数："+res.size());
    }
}
