
### 1、IoC 容器(工厂模式 + 反射)
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

##### IoC 之深入分析 Bean 的生命周期
1. bean 实例化 doCreateBean()
2. 激活 Aware
3. BeanPostProcessor: 在 Bean 实例化之后初始化之际对 Bean 进行增强处理
4. InitializingBean 和 init-method
5. DisposableBean 和 destroy-method
