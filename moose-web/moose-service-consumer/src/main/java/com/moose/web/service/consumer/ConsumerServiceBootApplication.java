package com.moose.web.service.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ConsumerServiceBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerServiceBootApplication.class, args);
    }

}
