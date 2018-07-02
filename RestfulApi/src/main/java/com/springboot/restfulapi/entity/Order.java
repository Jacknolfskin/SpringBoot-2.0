package com.springboot.restfulapi.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    String id;
    String name;
    Date createDate;

}
