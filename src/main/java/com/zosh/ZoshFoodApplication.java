package com.zosh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZoshFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZoshFoodApplication.class, args);
		Environment env = context.getEnvironment();
		System.out.println("DB Username: " + env.getProperty("spring.datasource.username"));
	}

}
