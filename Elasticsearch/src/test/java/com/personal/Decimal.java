package com.personal;

import lombok.Data;

/**
 * @Author: Jacknolfskin
 * @Date: 2018/5/8 9:55
 * @Path: com.personal
 */
@Data
public class Decimal {

    //前小数
    private Float frontDecimalNum;

    //后数小数
    private Float backDecimalNum;

    /**
     * 名称
     */
    private Float decimalResult;


}
