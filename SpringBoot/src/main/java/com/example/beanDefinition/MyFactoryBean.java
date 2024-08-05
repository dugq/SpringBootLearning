package com.example.beanDefinition;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

@Component("myBean")
@Slf4j
public class MyFactoryBean extends AbstractFactoryBean<TestBean> {

    @Override
    public Class<?> getObjectType() {
        return TestBean.class;
    }

    @Override
    protected TestBean createInstance() throws Exception {
        log.info("create instance of TestBean");
        return new TestBean("factory-bean-generated");
    }
}
