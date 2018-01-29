package com.moose.web.service.consumer.service;

import com.moose.web.service.consumer.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("moose-service-user")
public interface ConsumerService {

    @GetMapping("/user/{userId}")
    User findById(@PathVariable("userId") Long userId);

}
