* 父项目下的所有子项目都是SpringBoot的子项目测试，难免需要一些公用配置，
* 为了减少配置的重复，本项目提供common包，为其他项目负责。不做为学习内容

# SpringBoot特性
* SpringBoot依赖SpringContext等组件，这里并不能单独只关心SpringBoot，SpringIOC相关的也会在一起的。


#### bean生成的方式
##### IOC重要的顶层接口
* BeanFactory : 用于注册并管理Bean.
* FactoryBean : 用于创建对象： [示例](..%2Fjava%2Fcom%2Fexample%2FbeanDefinition%2FMyFactoryBean.java)
* BeanDefinitionRegistry : 用于根据BeanDefinition注册bean并实例化对象
* BeanDefinition ： 用于定义对象的元数据 [示例](..%2Fjava%2Fcom%2Fexample%2FbeanDefinition%2FbeanDefinition%2FBeanDefinitionTest.java)

###### 区别
* BeanFactory 相当于容器，容纳所有的bean，而bean的创建是由FactoryBean和BeanDefinitionRegistry等初始化的
* 而BeanDefinition是Bean的原数据，方便在BeanDefinitionRegistry中初始化数据。
* spring将bean的管理分为了几个部分： 
  * 容器： 存储所有的bean。例如：BeanFactory
  * 工厂： 用于生产bean的实例，例如：FactoryBean 、BeanDefinitionRegistry
* spring将bean的创建分为了三步：
  * 第一步： 定义. 例如： 根据BeanDefinition创建BeanDefinition对象，并注册到BeanDefinitionRegistry中。
  * 第二步： 对BeanDefinition进行后置处理。BeanDefinitionRegistryPostProcessor、BeanFactoryPostProcessor等
  * 第二步： 初始化。 根据BeanDefinition创建Bean实例。
  * 第三步： 后置处理。 例如： PostProcessor 、 BeanPostProcessor 等。

#### postProcessor
* BeanFactoryPostProcessor 
* BeanDefinitionRegistryPostProcessor
* BeanPostProcessor


### 重要注解
#### @Configuration
* 标注的类在Spring框架中会被CGLIB代理，以确保其中的@Bean方法返回的Bean是单例的，并且可以在运行时被Spring容器管理。这个代理对象的生命周期与Spring容器的生命周期紧密相关。以下是@Configuration标注的类生成的代理对象的生命周期的详细解析：
##### 生命周期阶段
###### 实例化：
* 当Spring容器启动时，会扫描所有的@Configuration类，并使用CGLIB生成代理类。
* 代理类会被实例化，并由Spring容器管理。
###### 初始化：
* 在代理对象实例化之后，Spring容器会调用@Configuration类中的@Bean方法，创建和配置Bean。
* 如果@Configuration类实现了InitializingBean接口，Spring容器会在所有属性设置完成后调用afterPropertiesSet方法。
* 此外，还可以使用@PostConstruct注解的方法来进行初始化操作。
###### 运行时：
* 在Spring容器运行期间，代理对象会拦截对@Bean方法的调用，确保每次调用都返回同一个单例Bean实例。
* 代理对象会管理这些Bean的生命周期，确保它们在需要时被创建和销毁。
###### 销毁：
* 当Spring容器关闭时，会销毁所有管理的Bean。
* 如果@Configuration类实现了DisposableBean接口，Spring容器会在销毁Bean之前调用destroy方法。
* 此外，还可以使用@PreDestroy注解的方法来进行销毁前的清理操作。


### Aware接口
* 一个标记超接口，指示 Bean 有资格通过回调样式的方法被 Spring 容器通知特定框架对象。
* 实际的方法签名由各个子接口确定，但通常应仅包含一个接受单个参数的 void-return 方法。
* ApplicationContextAwareProcessor 会为bean注入到Bean中。
* [示例](..%2Fjava%2Fcom%2Fexample%2FbeanDefinition%2Faware%2FCustomAware.java)



### spring的阶段
* 实例化ApplicationContext
* 加载所有的Configuration。这里有个误区，Configuration并不是Bean，而是ConfigurationClass。声明的@Bean变更为BeanDefinition,注册到Registry中。
* 解析配置并注册BeanDefinition 包含各种Bean的定义方式：@Bean、@Component @Import、BeanDefinition 等
* 实例化一些特殊的bean 例如：BeanFactoryPostProcessor、BeanPostProcessor等
* 根据需求创建Bean并初始化
  * 先实例化Bean
  * 再调用BeanPostProcessor的postProcessBeforeInitialization方法
  * 再调用InitializingBean的afterPropertiesSet方法
  * 最后调用BeanPostProcessor的postProcessAfterInitialization方法