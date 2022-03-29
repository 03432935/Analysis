package com.analysis.service.enums;

/**
 * @description:数据预测枚举
 * @author: lingwanxian
 * @date: 2022/3/15 17:45
 */
public enum PredictionStrategyEnum {
    //预测策略
    /*
    常见的预测算法有
    1.简易平均法，包括几何平均法、算术平均法及加权平均法;
    2.移动平均法，包括简单移动平均法和加权移动平均法;
    3,指数平滑法，包括一次指数平滑法和二次指数平滑法，三次指数平滑法;
    4,线性回归法，包括一元线性回归和二元线性回归
     */
    /**
     * 策略一：未命名
     */
    One("1","function1"),
    ;

    PredictionStrategyEnum(String code,String name){
        this.code = code;
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    /**
     * 根据code查找
     * @param code 枚举code
     * @return 枚举对象
     */
    public static PredictionStrategyEnum findEnumByCode(String code) {
        for (PredictionStrategyEnum statusEnum : PredictionStrategyEnum.values()) {
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
    public static PredictionStrategyEnum findEnumByName(String name) {
        for (PredictionStrategyEnum statusEnum : PredictionStrategyEnum.values()) {
            if (statusEnum.getName().equals(name)) {
                //如果需要直接返回code则更改返回类型为String,return statusEnum.code;
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("name is invalid");
    }

    private final String code;
    private final String name;

    @Override
    public String toString() {
        return "PredictionStrategyEnum{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
