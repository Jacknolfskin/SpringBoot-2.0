package com.springboot.restfulapi.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "content-security")
public class ContentSecurityConfig {

    private String secretId;

    private String secretKey;

    private String apiUrl;

    private String version;

    private String callbackUrl;

    private String title;
}
