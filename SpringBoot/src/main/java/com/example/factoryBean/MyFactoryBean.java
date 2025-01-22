package com.example.factoryBean;

import com.example.entry.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyFactoryBean implements FactoryBean<User> {
    @Override
    public User getObject() throws Exception {
        log.info("create bean user");
        return new User();
    }

    public MyFactoryBean() {
        log.info("create bean userFactoryBean");
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
