package com.elasticsearch.springdata.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Jacknolfskin on 2017/11/12.
 */
@Data
public class Tutorial implements Serializable {
    private Long id;
    /**
     * 教程名称
     */
    private String name;
}
