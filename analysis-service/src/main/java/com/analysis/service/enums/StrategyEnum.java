package com.analysis.service.enums;

import java.util.ArrayList;
import java.util.List;

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

    //还有个enum.tolist方法

    /**
     * 根据code查找
     * @param code 枚举code
     * @return 枚举对象
     */
    public static StrategyEnum findEnumByCode(String code) {
        for (StrategyEnum statusEnum : StrategyEnum.values()) {
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
    public static StrategyEnum findEnumByName(String name) {
        for (StrategyEnum statusEnum : StrategyEnum.values()) {
            if (statusEnum.getName().equals(name)) {
                //如果需要直接返回code则更改返回类型为String,return statusEnum.code;
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("name is invalid");
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
