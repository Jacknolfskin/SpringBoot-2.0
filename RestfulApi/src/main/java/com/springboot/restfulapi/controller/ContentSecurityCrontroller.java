package com.springboot.restfulapi.controller;

import com.alibaba.fastjson.JSON;
import com.springboot.restfulapi.entity.Antispam;
import com.springboot.restfulapi.entity.ResponseData;
import com.springboot.restfulapi.entity.SecurityCheck;
import com.springboot.restfulapi.service.ContentSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/content/security")
public class ContentSecurityCrontroller {

    @Resource
    private ContentSecurityService contentSecurityService;

    @PostMapping("/check")
    public ResponseData<Antispam> check(@RequestBody SecurityCheck securityCheck) {
        log.info("入参:{}", JSON.toJSONString(securityCheck));
        return contentSecurityService.check(securityCheck);
    }

    @PostMapping("/callback")
    public ResponseData<String> callback(@RequestParam Map<String, String> map) {
        log.info("回调结果:{}", JSON.toJSONString(map));
        return contentSecurityService.callback(map);
    }

}
