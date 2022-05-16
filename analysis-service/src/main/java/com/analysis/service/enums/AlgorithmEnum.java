package com.analysis.service.enums;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/15 16:21
 */
public enum AlgorithmEnum {
    /**
     * 3-sigma算法
     */
    THREE_SIGMA("1", "threeSigma"),

    /**
     * grubbs 测试算法
     */
    GRUBBS_TEST("2","grubbsTest"),

    /**
     * LOF算法
     */
    LOF("3","lof"),

    /**
     * iForest 算法
     */
    IFOREST("4","iForest"),

    /**
     *holtWinters指数算法
     */
    HOLTWINTERS("5","holtWinters"),

    /**
     * ARMA算法
     */
    ARMA("6","arma"),

    /**
     * Random Forest随机森林算法
     */
    RANDOMFOREST("7","randomForest"),


    ;

    private String code;
    private String name;

    AlgorithmEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
