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
    ~~~java
        /*
         * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient instance.
         */
        public @Bean MongoClient mongoClient() {
            return MongoClients.create("mongodb://localhost:27017");
        }
    ~~~
3. MongoClientFactoryBean SpringData提供的工厂模式
   * SpringFactoryBean, 用于构建MongoClient实例。包含了很多默认配置。比自己构建的MongoClient更方便。
    ~~~java
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
    //也使用时直接注入
    @Autowired
    private MongoClient mongoClient;     
    ~~~
4. MongoDbFactory
    ~~~java
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
    ~~~
5. MongoTemplate
   * springBoot提供的封装类，用于操作mongo数据库。内部代理了MongoClient
   * 依赖于SimpleMongoClientDatabaseFactory构建对象。也可以使用MongoClient构建对象
   * MongoClient可以只声明到
   ~~~java
        //
        @Autowired(required = false)
        private MongoTemplate mongoTemplate;
    
        @GetMapping("listByTemplate")
        public String listByTemplate(){
            if (mongoTemplate == null){
                return "No mongoTemplate found";
            }
            // 使用 springBoot 包装方法
            //内部使用反射实现
            List<Person> list = mongoTemplate.findAll(Person.class);
            return gson.toJson(list);
        }
    
        @GetMapping("listByTemplate2")
        public String listByTemplate2(){
            if (mongoTemplate == null){
                return "No mongoTemplate found";
            }
            // 使用execute进入原生模式
            return mongoTemplate.execute("first_collection",collection -> {  LinkedList<Document> list = collection.find().into(new LinkedList<>());
                String result = list.stream().map(Document::toJson).collect(Collectors.joining(","));
                return "["+result+"]";});
        }
   ~~~
* MongoMappingContext
* MappingMongoConverter
* MongoDbFactorySupport
* MongoCustomConversions

* Repositories
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



[参考手册](https://www.springcloud.cc/spring-data-mongodb.html#mongodb-getting-started)<br/>
[高版本手册](https://docs.spring.io/spring-data/mongodb/reference/mongodb/template-config.html)