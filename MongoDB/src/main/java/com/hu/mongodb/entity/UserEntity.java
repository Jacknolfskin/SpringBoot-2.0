package com.hu.mongodb.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private Long id;
    private String userName;
    private String passWord;
}
