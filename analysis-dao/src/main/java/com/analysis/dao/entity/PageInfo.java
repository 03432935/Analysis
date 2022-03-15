package com.analysis.dao.entity;


import com.analysis.dao.validator.annotation.LongNotNull;
import lombok.Data;

/**
 * @description:用于分页
 * @author: lingwanxian
 * @date: 2022/3/4 15:37
 */
@Data
public class PageInfo<T> {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 当前页
     */
    @LongNotNull(message = Messages.PAGE_NOT_NULL)
    private Long currentPage = 1L;

    /**
     * 页大小
     */
    @LongNotNull(message = Messages.SIZE_NOT_NULL)
    private Long pageSize = 10L;
}