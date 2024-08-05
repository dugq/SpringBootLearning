package com.example.beanDefinition.aware;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.*;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.context.support.ApplicationContextAwareProcessor} 会为所有的bean注入他们声明的属性。
 */
@Slf4j
@Component
public class CustomAware implements
        ApplicationEventPublisherAware,
        ResourceLoaderAware,
        BeanFactoryAware,
        EnvironmentAware,
        ImportAware,
        BeanNameAware,
        ApplicationContextAware {

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;
    private Environment environment;
    private ResourceLoader resourceLoader;
    private String beanName;
    private AnnotationMetadata annotationMetadata;

    @PostConstruct
    public void init() {
        log.info("CustomAware init");

    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanFactory parentBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationMetadata = importMetadata;
    }
}
