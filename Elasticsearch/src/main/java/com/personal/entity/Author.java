package com.personal.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Jacknolfskin on 2017/11/12.
 */
@Data
public class Author implements Serializable {
    /**
     * 作者id
     */
    private Long id;
    /**
     * 作者姓名
     */
    private String name;
    /**
     * 作者简介
     */
    private String remark;
}
