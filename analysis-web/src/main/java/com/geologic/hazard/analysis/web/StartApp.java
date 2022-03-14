package com.geologic.hazard.analysis.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = "com")
@EnableAsync
public class StartApp {

    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }

}
