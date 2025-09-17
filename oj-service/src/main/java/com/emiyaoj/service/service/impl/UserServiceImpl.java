package com.emiyaoj.service.service.impl;

import com.emiyaoj.service.domain.pojo.User;
import com.emiyaoj.service.mapper.UserMapper;
import com.emiyaoj.service.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
