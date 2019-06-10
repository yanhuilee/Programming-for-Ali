ApplicationContext上下文的初始化主要是进行了初始化BeanFactory，并在此基础上进行功能拓展比如事件机制，后处理器等，最后对BeanFactory中的所有bean进行实例化和依赖注入．

ApplicationContext 提供了事件监听的功能：
```java
// 创建 Bean 用来监听在 ApplicationContext 中发布的事件。
// 如果一个 Bean 实现了 ApplicationListener 接口，当一个 ApplicationEvent 被发布后
// Bean 会自动被通知
class AllApplicationEventListner implements ApplicationListener<ApplicationEvent> {
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// process event
	}
}
```

##### Spring 提供了五种标准事件：
- 上下文更新事件（ContextRefreshedEvent）:

```
// AbstractApplicationContext
refresh() {
	prepareBeanFactory() {//刷新前的预处理
		initPropertySources() //模板方法
	}
	obtainFreshBeanFactory() {}
	
	prepareBeanFactory() {}

	PostProcessBeanFactory() {}
	// 5、执行 BeanFactoryPostProcessor
	invokeBeanFactoryPostProcessors() {}
	// 6、注册
	registerBeanFactoryPostProcessors()
	// 7、
	initMessageSource()
	// 初始化事件派发器、监听器等
}
```