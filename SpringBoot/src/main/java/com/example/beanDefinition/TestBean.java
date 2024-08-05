package com.example.beanDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestBean {

    private final String name;

    public void init(){
        log.info("Initializing TestBean");
    }

    public TestBean(String name) {
        log.info("Creating TestBean with name: "+name);
        this.name = name;
    }

    public String sayHello() {
        return "Hello World! my name is "+name;
    }
}
