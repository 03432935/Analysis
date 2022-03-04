package com.analysis.dao.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/4 15:24
 * 父类，注意这个类不要让 mybatis-plus 扫描到(?
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    // 这里可以放一些公共的方法
}