/**
 * @(#)ThreadHolder.java, 2018-06-06.
 * <p>
 * Copyright 2018 Stalary.
 */
package com.personal.asyncsendmail.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ThreadHolder
 */
@Data
@AllArgsConstructor
public class ThreadHolder {

    /**
     * 线程id
     */
    private Integer id;

    /**
     * 线程名称
     */
    private String name;

    /**
     * 线程状态
     */
    private Thread.State state;
}