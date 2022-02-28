package com.analysis.service.enums;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 16:57
 */
public enum StrategyEnum {

    /**
     * 策略一，漏的数据取平均值
     */
    AVGSTRATEGY("1","avg"),
    ;


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final String code;
    private final String name;

    StrategyEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "StrategyEnum{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
