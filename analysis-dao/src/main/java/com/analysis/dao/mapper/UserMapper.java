package com.analysis.dao.mapper;

import com.analysis.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/18 20:06
 */
@Mapper
public interface UserMapper {

    int insert(User user);

    User selectById(int id);
}
