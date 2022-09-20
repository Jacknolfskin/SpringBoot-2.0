package com.springboot.restfulapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class})
public class Ch10Application {

	public static void main(String[] args) {
		SpringApplication.run(Ch10Application.class, args);
	}
}
