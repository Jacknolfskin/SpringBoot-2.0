package com.personal.example.conf;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConf implements RestTemplateCustomizer {

	@Override
	public void customize(RestTemplate restTemplate) {
		OkHttp3ClientHttpRequestFactory okHttp = (OkHttp3ClientHttpRequestFactory)restTemplate.getRequestFactory();
		okHttp.setReadTimeout(5000);
		okHttp.setWriteTimeout(3000);
		okHttp.setConnectTimeout(3000);
	}


}
