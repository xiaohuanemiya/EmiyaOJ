package com.emiyaoj.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
 * 用户权限关联表 Mapper 接口
 * </p>
 *
 * @author EmiyaOJ
 * @since 2025-09-17
 */
@Mapper
public interface UserPermissionMapper extends BaseMapper<UserPermission> {

    /**
     * 根据用户ID查询直接权限ID列表
     */
    List<Long> selectPermissionIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID删除用户权限关联
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和权限ID查询用户权限
     */
    UserPermission selectByUserIdAndPermissionId(@Param("userId") Long userId, @Param("permissionId") Long permissionId);

    /**
     * 根据用户ID查询被拒绝的权限ID列表
     */
    List<Long> selectDeniedPermissionIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询被授予的权限ID列表
     */
    List<Long> selectGrantedPermissionIdsByUserId(@Param("userId") Long userId);
}
