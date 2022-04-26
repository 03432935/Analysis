package com.analysis.service.enums;

/**
 * @description:数据预测枚举
 * @author: lingwanxian
 * @date: 2022/3/15 17:45
 */
public enum PredictionStrategyEnum {
    //预测策略
    /*
    时序数据分为平稳序列以及非平稳序列，
    其中平稳序列不存在规律随机波动，非平稳序列包含规律以及趋势
     */
    /**
     * 策略一：未命名
     */
    ARMA("1","ARMA"),
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
