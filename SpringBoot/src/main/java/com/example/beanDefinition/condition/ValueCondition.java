package com.example.beanDefinition.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ValueConditionImpl.class)
public @interface ValueCondition {
    String region();
    String value();
}
