package com.analysis.service.service.impl.AnomalyDetectServiceImpl.RandomForest;

import com.analysis.service.service.AnomalyDetectService;
import com.analysis.service.utils.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/20 16:16
 */
public class RandomForestImpl implements AnomalyDetectService {
    private ArrayList<Result> results;

    private static double[][] test_instances = null;
    private static int[] test_targets = null;

    @Override
    public void timeSeriesAnalyse(double[] data) {
        test_instances = new double[][]{{0, 0, 0, 1}, {1, 1, 1, 0}};
        ;
//        test_targets = new int[]{2,4,8};
        test_targets = new int[]{0, 7};
        double[][] instances = new double[][]{{0, 0, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 1},
                {0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1}, {1, 0, 0, 0}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 0, 1, 1},
                {1, 1, 0, 0}, {1, 1, 0, 1}, {1, 1, 1, 1}};

//        int[] targets = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[] targets = new int[]{0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7};
        RandomForest rf = new RandomForest();
        rf.train(instances, targets, 30, 4, 4, 12);
        for (int i = 0; i < rf.trees.length; i++) {
            RandomForest.printTree(rf.trees[i], String.valueOf(i));
        }

        //System.out.println(rf.trees_.length);
        int correct = 0;
        int wrong = 0;
        for (int i = 0; i < test_targets.length; i++) {
            int actual = rf.predicateBySavedData(test_instances[i]);
            System.out.println("actual: " + actual + ", expected: " + test_targets[i]);
            if (actual == test_targets[i]) {
                correct++;
            } else {
                wrong++;
            }
        }
        System.out.println("correct:" + correct + ", wrong:" + wrong + ", accuracy:" + 1.0
                * correct / (correct + wrong));

    }

    @Override
    public List<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

}
