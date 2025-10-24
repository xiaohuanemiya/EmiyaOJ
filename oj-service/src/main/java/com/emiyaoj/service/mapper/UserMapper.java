package com.emiyaoj.service.mapper;

import com.emiyaoj.service.domain.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "permissions", column = "id", javaType = List.class,
                    many = @Many(select = "com.emiyaoj.service.mapper.PermissionMapper.findByUid"))
    })
    Optional<User> findByUsername(String username);
}
