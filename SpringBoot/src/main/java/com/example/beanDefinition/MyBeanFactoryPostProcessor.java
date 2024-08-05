package com.example.beanDefinition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("dugq ----------------------- bean factory post processor");
        BeanDefinition myBean = beanFactory.getBeanDefinition("myBean");
        //可以对已经注册的BeanDefinition进行修改
        myBean.setInitMethodName("init");
        //官方不推荐这么做
//        beanFactory.registerSingleton("myBean", new TestBean("test"));
    }




}
