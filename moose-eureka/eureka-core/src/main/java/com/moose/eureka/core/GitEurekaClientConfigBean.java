package com.moose.eureka.core;

import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;

import java.util.List;

public class GitEurekaClientConfigBean extends EurekaClientConfigBean {

    private boolean enableGitEurekaServerUrlUpdate = false;

    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        List<String> urls = super.getEurekaServerServiceUrls(myZone);
        System.err.println(" update eureka server serviceUrls: " + urls + " enableGitEurekaServerUrlUpdate: "+ enableGitEurekaServerUrlUpdate);
        if(enableGitEurekaServerUrlUpdate) {
            return urls;
        }
        return urls;
    }

    public boolean isEnableGitEurekaServerUrlUpdate() {
        return enableGitEurekaServerUrlUpdate;
    }

    public void setEnableGitEurekaServerUrlUpdate(boolean enableGitEurekaServerUrlUpdate) {
        this.enableGitEurekaServerUrlUpdate = enableGitEurekaServerUrlUpdate;
    }

}
