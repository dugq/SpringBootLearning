package com.dugq;

import com.example.beanDefinition.condition.ValueCondition;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ValueCondition(region = "dugq.demo.strategy.mongo",value = "")
public @interface MongoSelector {

    @AliasFor(annotation = ValueCondition.class, attribute = "value")
    String value();
}
