* 父项目下的所有子项目都是SpringBoot的子项目测试，难免需要一些公用配置，
* 为了减少配置的重复，本项目提供common包，为其他项目负责。不做为学习内容

# SpringBoot特性
* SpringBoot依赖SpringContext等组件，这里并不能单独只关心SpringBoot，SpringIOC相关的也会在一起的。


#### bean生成的方式
##### IOC重要的顶层接口
* BeanFactory : 用于注册并管理Bean.
* FactoryBean : 用于创建对象： [示例](..%2Fjava%2Fcom%2Fexample%2FbeanDefinition%2FMyFactoryBean.java)
* BeanDefinitionRegistry : 用于根据BeanDefinition注册bean并实例化对象
* BeanDefinition ： 用于定义对象的元数据

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

