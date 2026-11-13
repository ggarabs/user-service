package com.github.ggarabs.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

@Bean
org.springframework.web.client.RestClient.Builder restClientBuilder() {
    return org.springframework.web.client.RestClient.builder();
}
}
