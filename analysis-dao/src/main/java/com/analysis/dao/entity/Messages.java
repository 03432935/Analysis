package com.analysis.dao.entity;

/**
 * @description:消息模板
 * @author: lingwanxian
 * @date: 2022/3/4 15:47
 */
public interface Messages {
    /**
     * 注解默认
     */
    String CK_NOT_BLANK_DEFAULT = "can not be blank";
    String CK_NOT_NULL_DEFAULT = "can not be null";
    String CK_NUMERIC_DEFAULT = "must be a number";
    String CK_RANGE_DEFAULT = "should be an integer,between {min} and {max}";
    String ID_NOT_NULL = "can not be null";
    String PAGE_NOT_NULL = "page not be null";
    String SIZE_NOT_NULL = "size not be null";
}
