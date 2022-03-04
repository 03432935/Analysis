package com.analysis.service.service.impl;

import com.analysis.dao.mybatis.SuperMapper;
import com.analysis.service.service.SuperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/4 15:22
 * SuperService 实现类（ 泛型：M 是  mapper(dao) 对象，T 是实体 ）
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements SuperService<T> {

}
