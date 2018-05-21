package com.personal;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Jacknolfskin
 * @Date: 2017/11/27 11:32
 * @Path: com.zhizhao.learn.model.resp.vo
 */
@Data
public class Question extends Denominator implements Serializable {
    //前数
    private Integer frontNum;

    //符号
    private Integer sign;

    //后数
    private Integer backNum;

    //结果
    private Integer result;


}
