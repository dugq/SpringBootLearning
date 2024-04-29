package com.dugq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


@Configuration
public class MongoConfiguration {

    /**
     * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient instance.
     */
    @MongoSelector(value = "mongoClient")
    public @Bean MongoClient mongoClientDefault() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    /**
     * Factory bean that creates the com.mongodb.Mongo instance
     * FactoryBean<MongoClient>
     */
    @MongoSelector(value = "clientFactory")
    public @Bean MongoClientFactoryBean mongoClientFactoryBean() throws Exception {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost("localhost");
        mongo.setPort(27017);
        return mongo;
    }

    @MongoSelector(value = "clientFactory")
    public @Bean MongoTemplate customTemplate() throws Exception {
        return new MongoTemplate(mongoClientFactoryBean().getObject(),"test");
    }

    /**
     * FactoryBean<MongoClient>
     * @return
     */
    @MongoSelector(value = "databaseFactory")
    public @Bean SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory(){
        return new SimpleMongoClientDatabaseFactory(MongoClients.create("mongodb://localhost:27017"),"test");
    }

    @MongoSelector(value = "databaseFactory")
    public @Bean MongoTemplate mongoTemplate(SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory){
        return new MongoTemplate(simpleMongoClientDatabaseFactory);
    }



}
