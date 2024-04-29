# mongo基础
* database
  * 数据库，类似mysql中的database 一般一个client链接一个database
* collection
  * 集合，类似mysql中的table 一个database下可以有多个collection
* index
  * 索引
* document
  * 文档，类似mysql中的row 一个collection下可以有多个document
  * 一般会为同一个collection下的文本创建一个java实例


# SpringDataMongo auto-config
* MongoAutoConfiguration
  * mongoClient autoConfig类
* MongoDataAutoConfiguration
  * mongoTemplate相关 autoConfig类

# 核心组件
### Mono客户端
1. com.mongodb.Mongo 高版本此类已经被MongoClient替代
2. com.mongodb.MongoClient 
   *  Mongo的子类。
   * SpringData大部分使用的是此类
~~~ java
    /*
     * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient instance.
     */
    public @Bean MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
~~~
3. MongoClientFactoryBean SpringData提供的工厂模式
   * SpringFactoryBean, 用于构建MongoClient实例
   * 它的优势在于
~~~ java
    /*
     * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient instance.
     */
    public @Bean MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
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


# SpringBoot 构建步骤
MongoProperties ： properties属性配置类
##### 启动配置： MongoDataAutoConfiguration.java
1. MongoAutoConfiguration.java 
   * 引入MongoProperties构建MongoPropertiesClientSettingsBuilderCustomizer实例。
     * 可以有多分，按优先级覆盖.通常base-api需要统一定制某些配置时使用。
   * 构建MongoClientSettings实例，加载默认配置
     * @ConditionOnMissingBean注解，所以可自定义进行覆盖
   * 构建MongoClient实例，并注入到MongoTemplate实例中
     * @ConditionOnMissingBean注解，所以可自定义进行覆盖
2. MongoDataConfiguration
   3. 
3. MongoDatabaseFactoryConfiguration
4. MongoDatabaseFactoryDependentConfiguration

# 扩展组件



[参考手册](https://www.springcloud.cc/spring-data-mongodb.html#mongodb-getting-started)