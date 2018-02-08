package com.apass.zufang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import com.apass.gfb.framework.BootApplication;

@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}
