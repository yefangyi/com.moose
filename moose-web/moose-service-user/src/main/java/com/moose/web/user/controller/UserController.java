package com.moose.web.user.controller;

import com.moose.web.user.entity.User;
import com.moose.web.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public User findById(@PathVariable Long userId) {
        Assert.notNull(userId, "userId must be bot null");
        return userService.findById(userId);
    }

}
