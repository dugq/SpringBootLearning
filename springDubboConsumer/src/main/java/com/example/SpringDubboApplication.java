package com.example;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class SpringDubboApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDubboApplication.class,args);
        System.out.println("Spring Dubbo Application is running!");
    }
}