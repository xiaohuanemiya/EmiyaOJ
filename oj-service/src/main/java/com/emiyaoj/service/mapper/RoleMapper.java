package com.emiyaoj.service.mapper;

import com.emiyaoj.service.domain.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleCodesByIds(@Param("roleIds") List<Long> roleIds);
}
