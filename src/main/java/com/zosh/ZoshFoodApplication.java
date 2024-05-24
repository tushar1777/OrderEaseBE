package com.zosh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ZoshFoodApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ZoshFoodApplication.class, args);
        Environment env = context.getEnvironment();
        System.out.println("DB Username: " + env.getProperty("spring.datasource.username"));
    }
}
