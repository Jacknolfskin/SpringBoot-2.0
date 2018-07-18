package com.personal.asyncsendmail.service;

import com.personal.asyncsendmail.dao.BaseRepo;

import java.util.List;

/**
 * BaseService
 */
public abstract class BaseService<T, R extends BaseRepo<T, Long>> {

    protected R repo;

    BaseService() {

    }

    BaseService(R repo) {
        this.repo = repo;
    }

    public T findOne(Long id) {
        return repo.getOne(id);
    }

    public T save(T entity) {
        return repo.save(entity);
    }

    public void save(List<T> entityList) {
        repo.saveAll(entityList);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public void delete(T entity) {
        repo.delete(entity);
    }

}