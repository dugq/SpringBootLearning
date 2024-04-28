package com.dugq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.net.UnknownHostException;

public class MongoConfiguration {


    /*
     * Factory bean that creates the com.mongodb.Mongo instance
     */
    public @Bean MongoClientFactoryBean mongo2() {
        AbstractMongoClientConfiguration clientConfiguration = new AbstractMongoClientConfiguration() {
            @Override
            protected String getDatabaseName() {
                return "";
            }
        };

        MongoClientFactoryBean mongo = new MongoClientFactoryBean(clientConfiguration);
        mongo.setHost("localhost");
        return mongo;
    }

    public @Bean SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory(MongoClient mongoClient){
        return new SimpleMongoClientDatabaseFactory(mongoClient);
    }

    public @Bean MongoTemplate mongoTemplate(SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory){
        return new MongoTemplate(simpleMongoClientDatabaseFactory);
    }

}
