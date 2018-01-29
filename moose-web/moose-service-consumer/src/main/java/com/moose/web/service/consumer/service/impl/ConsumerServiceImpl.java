package com.moose.web.service.consumer.service.impl;

import com.moose.web.service.consumer.entity.User;
import com.moose.web.service.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public User findById(Long userId) {
        return restTemplate.getForObject("http://moose-service-user/user/" + userId, User.class);
    }

}
