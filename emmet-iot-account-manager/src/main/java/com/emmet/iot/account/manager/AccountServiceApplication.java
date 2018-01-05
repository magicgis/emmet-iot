package com.emmet.iot.account.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;


@SpringBootApplication
@EnableSpringHttpSession
@Configuration
public class AccountServiceApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}
	
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		return new HeaderHttpSessionStrategy();
	}
}
