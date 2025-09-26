package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.emiyaoj.common.utils.ObjectUtil;
import com.emiyaoj.service.domain.pojo.User;
import com.emiyaoj.service.domain.pojo.UserLogin;
import com.emiyaoj.service.mapper.PermissionMapper;
import com.emiyaoj.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    /**
     * 根据用户名查询用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.isEmpty()){
            throw new InternalAuthenticationServiceException("");
        }
        //  根据用户名查询用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        // 判断是否查到用户 如果没查到抛出异常
        if (ObjectUtil.isNull(user)){
            throw new UsernameNotFoundException("");
        }
        // 2.赋权操作 查询数据库
        List<Integer> list = permissionMapper.findCodeByUid(user.getId());

        int code = 0;
        for (int i : list) {
            code = code | i;
        }

        return new UserLogin(user, code);
    }
}
