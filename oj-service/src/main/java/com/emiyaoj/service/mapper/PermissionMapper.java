package com.emiyaoj.service.mapper;

import com.emiyaoj.service.domain.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据 用户id 查询权限
     * @param uid
     * @return
     */
    List<Permission> findByUid(Long uid);

    List<String> findCodeByUid(Long id);
}
