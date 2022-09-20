package com.springboot.restfulapi.service;


import com.springboot.restfulapi.entity.Antispam;
import com.springboot.restfulapi.entity.ResponseData;
import com.springboot.restfulapi.entity.SecurityCheck;

import java.util.Map;

public interface ContentSecurityService {


    ResponseData<Antispam> check(SecurityCheck securityCheck);

    ResponseData<String> callback(Map<String, String> map);

}
