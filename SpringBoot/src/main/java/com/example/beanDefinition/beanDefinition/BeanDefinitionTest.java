package com.example.beanDefinition.beanDefinition;

import com.example.beanDefinition.TestBean2;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class BeanDefinitionTest {

    public static BeanDefinition  genBeanDefinitionByBuilder(Class<?> beanType){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanType);
        builder.addConstructorArgValue("test bean 2");
        builder.setRole(BeanDefinition.ROLE_APPLICATION);
        return builder.getBeanDefinition();
    }

    public static BeanDefinition genBeanDefinitionByConstructor(Class<?> beanType){
        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        return beanDefinition;
    }

}
