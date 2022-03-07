package com.analysis.dao.mapper;

import com.analysis.dao.entity.User;
import com.analysis.dao.mybatis.SuperMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/18 20:06
 */
@Mapper
public interface UserMapper extends SuperMapper<User> {

    int insertf(User user);

    User selectById(int id);

    //@Param注解用于给参数取别名，如果只有一个参数且参数在<if>里使用，则必须加别名
}
