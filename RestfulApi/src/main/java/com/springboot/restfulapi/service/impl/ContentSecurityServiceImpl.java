package com.springboot.restfulapi.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.restfulapi.conf.ContentSecurityConfig;
import com.springboot.restfulapi.entity.Antispam;
import com.springboot.restfulapi.entity.ResponseData;
import com.springboot.restfulapi.entity.SecurityCheck;
import com.springboot.restfulapi.enums.FileContentTypeEnum;
import com.springboot.restfulapi.service.ContentSecurityService;
import com.springboot.restfulapi.utils.HttpClient4Utils;
import com.springboot.restfulapi.utils.SignatureUtils;
import com.springboot.restfulapi.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class ContentSecurityServiceImpl implements ContentSecurityService {

    @Resource
    private ContentSecurityConfig contentSecurityConfig;
    /**
     * 实例化HttpClient，发送http请求使用，可根据需要自行调参
     */
    private static HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 10000, 2000, 2000);

    @Override
    public ResponseData<Antispam> check(SecurityCheck securityCheck) {
        Map<String, String> params = new HashMap<>(16);
        // 1.设置公共参数
        params.put("secretId", contentSecurityConfig.getSecretId());
        params.put("version", contentSecurityConfig.getVersion());
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));
        params.put("signatureMethod", "MD5");
        // 2.设置私有参数
        params.put("title", contentSecurityConfig.getTitle());
        params.put("callbackUrl", contentSecurityConfig.getCallbackUrl());
        params.put("callback", securityCheck.getFileId());

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", FileContentTypeEnum.getValue(securityCheck.getFileTypeId()));
        jsonObject.put("data", securityCheck.getFileAbsoluteUrl());
        jsonObject.put("dataId", securityCheck.getFileId());
        jsonArray.add(jsonObject);
        params.put("content", jsonArray.toString());

        // 预处理参数
        params = Utils.pretreatmentParams(params);
        // 3.生成签名信息
        String signature = SignatureUtils.genSignature(contentSecurityConfig.getSecretKey(), params);
        params.put("signature", signature);

        log.info("内容安全检验入参：{}", JSON.toJSONString(params));
        // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
        String response = HttpClient4Utils.sendPost(httpClient, contentSecurityConfig.getApiUrl(), params, Consts.UTF_8);
        log.info("内容安全检验出参：{}", response);
        JSONObject jsonObjectResponse = JSONObject.parseObject(response).getJSONObject("result").getJSONObject("antispam");
        Antispam antispam = Antispam.builder().fileId(jsonObjectResponse.getString("callback")).taskId(jsonObjectResponse.getString("taskId")).build();
        return ResponseData.success(antispam);
    }

    @Override
    public ResponseData<String> callback(Map<String, String> map) {
        ResponseData<String> response = ResponseData.success();
        JSONObject jsonObject = JSONObject.parseObject(map.get("callbackData")).getJSONObject("antispam");
        if ("2".equals(jsonObject.getString("checkStatus"))) {
            String fileId = jsonObject.getString("fileId");
            String suggestion = jsonObject.getString("suggestion");
            log.info("文件Id:{},建议结果：{}", fileId, suggestion);
        }
        return response;
    }
}
