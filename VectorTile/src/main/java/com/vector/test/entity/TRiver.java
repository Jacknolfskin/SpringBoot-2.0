package com.vector.test.entity;


import cn.com.enersun.dgpmicro.entity.GeoEntity;

/*
 *
 * gen by beetlsql 2018-12-20
 */
public class TRiver implements GeoEntity<Integer> {

    private Integer id;
    private Integer type;
    private String name;
    private Object shape;

    public TRiver() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getShape() {
        return shape;
    }

    public void setShape(Object shape) {
        this.shape = shape;
    }


}
