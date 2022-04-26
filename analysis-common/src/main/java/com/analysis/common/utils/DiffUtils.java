package com.analysis.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/4/22 16:53
 */
public class DiffUtils {

    public static List<Double> diffArr(double[] arr) {
        List<Double> list = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
            list.add(arr[i] - arr[i - 1]);
        }
        return list;
    }
}
