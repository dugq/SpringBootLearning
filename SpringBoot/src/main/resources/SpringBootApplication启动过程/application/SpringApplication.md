# SpringBootApplication
## 简介
* spring application的启动引导类
* 通常使用SpringApplication的run静态方法进行启动，也可以自己创建，然后进行个性化处理

## 初始化
* 设置WebApplicationType
* 加载 META-INF/[spring.factories](../img/springFactories.md)
* 从spring.factories读取所有的BootstrapRegistryInitializer并添加到引导类的bootstrapRegistryInitializers中
* 从spring.factories读取所有的[ApplicationContextInitializer](context/ApplicationContext.md)并添加到引导类的initializers中
*  从spring.factories读取所有的[ApplicationListener](ApplicationListener.md)并添加到引导类的listeners中

## 引导启动
* 创建BootstrapContext (2.4以后才有的)
* 从SpringFactoriesLoader中读取自定义的[SpringApplicationRunListener](ApplicationListener.md)
* SpringApplicationRunListener#start
* [prepare environment](context/Environment.md)
* [createApplicationContext](context/ApplicationContext.md)
* BootstrapContext.closed
* [prepare context](context/ApplicationContext.md#准备过程)
* [refresh context](context/ApplicationContext.md#启动过程)


## 重要属性
* WebApplicationType
  * 通常是根据引入的jar包进行判断，servlet ， reactive 和 常规的application
  * 后续会根据WebApplicationType进行适配，选择不同的ApplicationContext
* bootstrapRegistryInitializers
  * 用于对BootstrapContext进行个性个性化设置。
  * BootstrapContext是2.4版本后加入的，生命周期也仅在初始化阶段，所以主要针对的是application
* initializers
  * ApplicationContext初始化定制
* applicationContextFactory
  
