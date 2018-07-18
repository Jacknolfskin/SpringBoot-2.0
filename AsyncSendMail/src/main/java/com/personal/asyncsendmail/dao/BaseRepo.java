package com.personal.asyncsendmail.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * BaseRepo
 */
@NoRepositoryBean
public interface BaseRepo<T, Long extends Serializable> extends JpaRepository<T, Long> {

}
