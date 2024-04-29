package com.example.beanDefinition.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.Objects;

public class ValueConditionImpl implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String name = ValueCondition.class.getName();
        Map<String, Object> valueCondition = metadata.getAnnotationAttributes(name);
        if (valueCondition == null){
            return false;
        }
        Object region = valueCondition.get("region");
        if(region == null){
            return false;
        }
        Object expected = valueCondition.get("value");
        String active =context.getEnvironment().getProperty(region.toString());
        return Objects.equals(expected, active);
    }
}
