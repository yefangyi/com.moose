package com.moose.web.service.consumer.service;

import com.moose.web.service.consumer.entity.User;

public interface ConsumerService {

    User findById(Long userId);
}
