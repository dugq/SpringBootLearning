package com.example.beanDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bean")
public class BeanDefinitionTest {

    @Autowired
    private TestBean testBean;
    @Autowired
    private TestBean2 testBean2;

    @GetMapping("/factory/say")
    public String say(){
        return testBean.sayHello();
    }

}
