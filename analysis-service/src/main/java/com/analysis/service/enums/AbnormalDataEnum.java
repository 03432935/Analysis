package com.analysis.service.enums;

/**
 * @description:数据异常枚举
 * @author: lingwanxian
 * @date: 2022/3/16 17:27
 */

public enum AbnormalDataEnum {

    /**
     * 正常数据默认值
     */
    NORMAL("0","normal"),

    /**
     * 数据异常大
     */
    BIG_ABNORMAL("1","bigAbnormal"),

    /**
     * 数据异常小
     */
    SMALL_ABNORMAL("2","smallAbnormal")

    ;
    AbnormalDataEnum(String code,String name){
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
    public static AbnormalDataEnum findEnumByCode(String code) {
        for (AbnormalDataEnum statusEnum : AbnormalDataEnum.values()) {
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
    public static AbnormalDataEnum findEnumByName(String name) {
        for (AbnormalDataEnum statusEnum : AbnormalDataEnum.values()) {
            if (statusEnum.getName().equals(name)) {
                //如果需要直接返回code则更改返回类型为String,return statusEnum.code;
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("name is invalid");
    }
}
