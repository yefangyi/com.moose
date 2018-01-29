package com.moose.web.user.service.Impl;

import com.google.common.collect.Maps;
import com.moose.web.user.entity.User;
import com.moose.web.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static Map<Long, User> userInfoCache = Maps.newHashMap();

    static {
        User user = new User();
        user.setId(1L);
        user.setUserName("张三");
        userInfoCache.put(1L, user);
    }

    @Override
    public User findById(Long id) {
        return userInfoCache.get(id);
    }

}
