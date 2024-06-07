package com.dugq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Created by dugq on 2024/4/27.
 */
@SpringBootApplication(exclude = {MongoDataAutoConfiguration.class, MongoAutoConfiguration.class})
public class MongoDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoDemoApplication.class,args);
    }
}
