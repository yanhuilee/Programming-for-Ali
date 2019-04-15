### 大纲
#### 1、IoC 容器(工厂模式 + 反射)
> 由 Spring 来负责控制对象的生命周期和对象间的关系。

##### BeanFactory：例化、配置和管理 Bean。
单例(Singleton)Bean 在启动时就会被 BeanFactory 实例化，其它的 Bean 在请求时创建

XmlBeanFactory: 从 XML 文件中读取 Bean 的定义信息

**注意**：BeanFactory 只能管理单例（Singleton）Bean 的生命周期。它不能管理原型(prototype,非单例)Bean 的生命周期。因为原型 Bean 实例被创建之后便被传给了客户端,容器失去了对它们的引用。

##### web 应用中创建 IoC 容器
```
web容器启动后加载web.xml，加载 ContextLoaderListener 监听器
触发 contextInitialized() --> initWebAppliationContext() 负责创建Spring容器（DefaultListableBeanFactory）
```

##### BeanFactory 和 ApplicationContext

大多数情况我们并不直接使用 BeanFactory，而是使用 ApplicationContext。

ApplicationContext: 在容器启动时，一次性创建所有的Bean。这样，在容器启动时，就可以发现Spring中存在的配置错误，有利于检查所依赖属性是否注入

##### Bean：
Bean 的生命周期由 IoC 容器控制。IoC 容器定义 Bean 操作的规则，即 Bean 的定义（BeanDefinition）

1、Bean 初始化
```
1. 资源定位：Resource
2. 装载和解析：BeanDefinition - BeanWrapper
3. 注册
```

2、加载 Bean
```
getBean()
获取 beanName
单例
原型模式：依赖检查
```

3、创建 Bean: 初始化 Bean 对象
```
createBeanInstance()
属性注入，依赖处理
initializeBean(): Aware，后置处理器，init()
```

作用域 scope
```
prototype

lazyinit dependson primary
```


##### IoC 之深入分析 Bean 的生命周期
1. bean 实例化 doCreateBean()
2. 激活 Aware
3. BeanPostProcessor: 在 Bean 实例化之后初始化之际对 Bean 进行增强处理
4. InitializingBean 和 init-method
5. DisposableBean 和 destroy-method


### 容器创建

BeanFactory
```
// AbstractApplicationContext
refresh() {
	prepareBeanFactory() {//刷新前的预处理
		initPropertySources() //模板方法
	}

	obtainFreshBeanFactory() {

	}

	prepareBeanFactory() {

	}

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



##### 总结
1. spring 容器在启动时，先会保存所有注册进来的 bean定义信息
	xml注册bean <bean />
	注解
2. spring 容器在合适的时机创建这些 bean
	用到这个bean 时，getBean(), 创建好后保存在容器中
	统一创建剩下的所有 bean，finishBeanFactoryInitialization()
3. 后置处理器：BeanPostProcessor
	每一个 bean 创建完成，都会使用各种后置处理器进行处理，来增强bean的功能
		AutowiredAnnotationBeanPostProcessor：处理自动注入
		AnnotationAwareAspectJAutoProxyCreator：AOP功能
		AsyncAnnotationBeanPostProcessor
4. 事件驱动模型
	ApplicationListener：事件监听
	ApplicationEventMulticaster：事件派发


#### 声明式事务
