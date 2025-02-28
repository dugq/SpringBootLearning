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
* ConfigurableEnvironment

## 准备过程
~~~java
class SpringApplication {
    private void prepareContext(DefaultBootstrapContext bootstrapContext, ConfigurableApplicationContext context,
                                ConfigurableEnvironment environment, SpringApplicationRunListeners listeners,
                                ApplicationArguments applicationArguments, Banner printedBanner) {
        context.setEnvironment(environment);
        // 模板方法，为子类开放定值能力. 主要给beanFactory设置了 beanNameGenerator resourceLoader 和  ConversionService
        //context.getBeanFactory().registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR,this.beanNameGenerator);
        // context.getBeanFactory().setConversionService(context.getEnvironment().getConversionService())
        postProcessApplicationContext(context);
        /**
         使用 所有的 Initializer 初始化 context 
         for (ApplicationContextInitializer initializer : getInitializers()) 
         initializer.initialize(context);
         **/
        applyInitializers(context);
        /**
            使用 所有的 listener 初始化 context 
            this.listeners.forEach(listener -> listener.contextPrepared(context))
         */
        listeners.contextPrepared(context);
        bootstrapContext.close(context);
        // Add boot specific singleton beans
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 注册启动参数器
        beanFactory.registerSingleton("springApplicationArguments", applicationArguments);
        // 设置是否允许循环调用 和 覆盖定义
        if (beanFactory instanceof AbstractAutowireCapableBeanFactory autowireCapableBeanFactory) {
            autowireCapableBeanFactory.setAllowCircularReferences(this.allowCircularReferences);
            if (beanFactory instanceof DefaultListableBeanFactory listableBeanFactory) {
                listableBeanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
            }
        }
        // 设置是否允许懒加载，如需要，增加懒加载的PostProcessor实现
        if (this.lazyInitialization) {
            context.addBeanFactoryPostProcessor(new LazyInitializationBeanFactoryPostProcessor());
        }
        // 添加一个PostProcessor，主要用于将注册的bean放入到Environment中
        context.addBeanFactoryPostProcessor(new PropertySourceOrderingBeanFactoryPostProcessor(context));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        Set<Object> sources = getAllSources();
        load(context, sources.toArray(new Object[0]));
        listeners.contextLoaded(context);
    }
}
~~~
## 启动过程


# ApplicationContextInitializer


# ApplicationContextFactory
