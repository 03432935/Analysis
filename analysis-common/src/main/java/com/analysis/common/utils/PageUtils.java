package com.analysis.common.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/19 20:03
 */
@Slf4j
public class PageUtils {

    public static<T> PageInfo<T> pageList(List<T> list, Integer pageNum, Integer pageSize){
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        // 封装list到PageInfo对象中自动分页
        PageInfo<T> poPageInfo = new PageInfo<>(list);
        log.info("开启分页");
        return poPageInfo;
    }
}
