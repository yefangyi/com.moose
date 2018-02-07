package com.moose.eureka.server;

import com.moose.eureka.core.GitEurekaClientConfigBean;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaClientConfig {

    @Bean
    public EurekaClientConfigBean eurekaClientConfigBean() {
        return new GitEurekaClientConfigBean();
    }

}
