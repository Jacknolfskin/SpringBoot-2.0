package com.hu.mongodb.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Auther: JackHu
 * @Date: 2018/8/23 22:35
 * @Description:
 */
@Data
public class Baike {
    private String id;
    private String desc;
    private List<String> tag;
    private Comment comment;
    private Date crateDate;
    private Date updateDate;
    private int status = 0;
}
