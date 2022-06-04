package com.analysis.service.enums;

/**
 * @description:数据补全枚举
 * @author: lingwanxian
 * @date: 2022/2/28 16:57
 */
public enum CompletionStrategyEnum {

    /**
     *原始数据默认标识
     */
    ORIGINAL("0","org"),

    /**
     * 策略一，漏的数据取平均值
     */
    AVGSTRATEGY("1","avg"),

    /**
     * 指数平滑
     */
    HOLTWINTERS("2","holtWinters"),

    /**
     * ARIMA
     */
    ARIMA("3","arima"),

    /**
     * RandomForest
     */
    RANDOMFOREST("4","randomForest")
    ;


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    //还有个enum.tolist方法

    /**
     * 根据code查找
     * @param code 枚举code
     * @return 枚举对象
     */
    public static CompletionStrategyEnum findEnumByCode(String code) {
        for (CompletionStrategyEnum statusEnum : CompletionStrategyEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                //如果需要直接返回name则更改返回类型为String,return statusEnum.name;
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("code is invalid");
    }

    /**
     * 根据name查找
     * @param name 枚举name
     * @return 枚举对象
     */
    public static CompletionStrategyEnum findEnumByName(String name) {
        for (CompletionStrategyEnum statusEnum : CompletionStrategyEnum.values()) {
            if (statusEnum.getName().equals(name)) {
                //如果需要直接返回code则更改返回类型为String,return statusEnum.code;
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("name is invalid");
    }


    private final String code;
    private final String name;

    CompletionStrategyEnum(String code, String name) {
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
