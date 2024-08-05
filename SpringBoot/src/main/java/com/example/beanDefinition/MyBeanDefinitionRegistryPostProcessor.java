package com.example.beanDefinition;

import com.example.beanDefinition.beanDefinition.BeanDefinitionTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
      log.info("dugq ----------------------- postProcessBeanDefinitionRegistry");
        // 可以获取beanDefinition然后进行修改
        //BeanDefinition beanDefinition = registry.getBeanDefinition("myBean");
        // 注册新的Bean定义
        BeanDefinition beanDefinition = BeanDefinitionTest.genBeanDefinitionByBuilder(TestBean2.class);
//        beanDefinition = BeanDefinitionTest.genBeanDefinitionByConstructor(TestBean2.class);
        registry.registerBeanDefinition("myBean2", beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("dugq  ----------------------- postProcessBeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("test - setApplicationContext");
    }
}
