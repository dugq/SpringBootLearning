package com.example.beanDefinition;

public class TestBean {

    private final String name;

    public TestBean(String name) {
        this.name = name;
    }

    public String sayHello() {
        return "Hello World! my name is "+name;
    }
}
