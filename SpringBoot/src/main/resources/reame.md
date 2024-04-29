* 父项目下的所有子项目都是SpringBoot的子项目测试，难免需要一些公用配置，
* 为了减少配置的重复，本项目提供common包，为其他项目负责。不做为学习内容

# SpringBoot特性
* SpringBoot依赖SpringContext等组件，这里并不能单独只关心SpringBoot，SpringIOC相关的也会在一起的。


#### bean生成的方式
* @ComponentScan 配合 @Bean @Configuration 注解