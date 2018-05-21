package com.personal;

import lombok.Data;

/**
 * @Author: Jacknolfskin
 * @Date: 2018/5/8 9:56
 * @Path: com.personal
 */
@Data
public class Denominator extends Decimal {

    //前数分母
    private Integer frontDenominatorNum;

    //后数分母
    private Integer backDenominatorNum;
}
