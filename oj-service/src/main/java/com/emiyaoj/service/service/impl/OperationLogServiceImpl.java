package com.emiyaoj.service.service.impl;

import com.emiyaoj.service.domain.pojo.OperationLog;
import com.emiyaoj.service.mapper.OperationLogMapper;
import com.emiyaoj.service.service.IOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
