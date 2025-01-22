# ApplicationContext

## createApplicationContext
~~~java
    protected ConfigurableApplicationContext createApplicationContext() {
		return this.applicationContextFactory.create(this.webApplicationType);
	}
~~~
* Application context由ApplicationContextFactory创建
~~~java
public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {
		try {
			return getFromSpringFactories(webApplicationType, ApplicationContextFactory::create,
					this::createDefaultApplicationContext);
		}
		catch (Exception ex) {
			throw new IllegalStateException("Unable create a default ApplicationContext instance, "
					+ "you may need a custom ApplicationContextFactory", ex);
		}
	}
~~~
* 默认使用DefaultApplicationContextFactory创建. springBoot默认配置了三种Factory，分别对应普通、servlet 和 reactive模式，根据webApplicationType进行选择


## 重要属性

## 准备过程

## 启动过程


# ApplicationContextInitializer


# ApplicationContextFactory
