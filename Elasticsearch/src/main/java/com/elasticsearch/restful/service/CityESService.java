package com.elasticsearch.restful.service;

import com.elasticsearch.restful.entity.Entity;

import java.util.List;

public interface CityESService {

    void saveEntity(Entity entity);

    void saveEntity(List<Entity> entityList);

    List<Entity> searchEntity(String searchContent);
}
