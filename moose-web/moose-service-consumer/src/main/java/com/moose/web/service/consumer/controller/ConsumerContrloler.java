package com.moose.web.service.consumer.controller;

import com.moose.web.service.consumer.entity.User;
import com.moose.web.service.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ConsumerContrloler {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/{userId}")
    public User findById(@PathVariable Long userId) {
        Assert.notNull(userId, "userId must be bot null");
        return consumerService.findById(userId);
    }

}
