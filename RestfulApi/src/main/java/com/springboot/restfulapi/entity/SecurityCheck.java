package com.springboot.restfulapi.entity;

import lombok.Data;

/**
 * @author HuFei
 */
@Data
public class SecurityCheck {

    String fileId;

    String fileAbsoluteUrl;

    Integer fileTypeId;
}
