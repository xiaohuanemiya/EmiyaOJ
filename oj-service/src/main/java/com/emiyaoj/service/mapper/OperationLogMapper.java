package com.emiyaoj.service.mapper;

import com.emiyaoj.service.domain.pojo.OperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}
