# mongo auto-config
* MongoAutoConfiguration
  * mongoClient autoConfig类
* MongoDataAutoConfiguration
  * mongoTemplate相关 autoConfig类

# 核心组件
* MongoDB
### Mono客户端
1. com.mongodb.Mongo
~~~ java
    @Bean
    public Mongo mongo(){
        return new Mongo("localhost");
    }
~~~
2. com.mongodb.MongoClient 
   *  Mongo的子类。
   * SpringData大部分使用的是此类
3. MongoClientFactoryBean SpringData提供的工厂模式
~~~ java
 public @Bean MongoClientFactoryBean mongo() {
          MongoClientFactoryBean mongo = new MongoClientFactoryBean();
          mongo.setHost("localhost");
          return mongo;
     }
//使用时直接注入
@Autowired
private MongoClient mongoClient;     
~~~
4. MongoDbFactory
~~~java
    public @Bean MongoDbFactory mongo3() throws Exception {
  return new SimpleMongoDbFactory(new MongoClient("localhost"), "database");
}

//使用时直接注入
@Autowired
private MongoClient mongoClient;  
~~~
* MongoMappingContext
* MappingMongoConverter
* MongoDbFactorySupport
* MongoCustomConversions
* MongoTemplate
* Repositories
* 
* @Document
* @Id
* GridFsTemplate

# 扩展组件



[参考手册](https://www.springcloud.cc/spring-data-mongodb.html#mongodb-getting-started)