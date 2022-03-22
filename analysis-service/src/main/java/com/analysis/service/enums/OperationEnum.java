package com.analysis.service.enums;

/**
 * @description:操作枚举
 * @author: lingwanxian
 * @date: 2022/3/16 17:54
 */
public enum OperationEnum {

    /**
     * 异常检测
     */
    ANOMALY_DETECTION("0","anomaly_detection"),

    /**
     * 数据补全
     */
    COMPLETION("1","completion"),

    /**
     * 数据预测
     */
    PREDICTION("2","prediction"),
    ;

    OperationEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

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
    public static OperationEnum findEnumByCode(String code) {
        for (OperationEnum statusEnum : OperationEnum.values()) {
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
    public static OperationEnum findEnumByName(String name) {
        for (OperationEnum statusEnum : OperationEnum.values()) {
            if (statusEnum.getName().equals(name)) {
                //如果需要直接返回code则更改返回类型为String,return statusEnum.code;
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("name is invalid");
    }
}
