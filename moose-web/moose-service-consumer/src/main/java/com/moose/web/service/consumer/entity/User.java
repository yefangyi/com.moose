package com.moose.web.service.consumer.entity;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
