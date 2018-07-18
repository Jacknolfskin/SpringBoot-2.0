package com.personal.asyncsendmail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author feihu5
 * @date 2018/7/18 17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Log extends BaseEntity {

    /**
     * 日志等级
     */
    private String level;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 日志种类(user，project)
     */
    private String type;

    /**
     * 通用id
     */
    private Long commonId;

    /**
     * 次数
     */
    private Integer count = 1;
}