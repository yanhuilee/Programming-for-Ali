> 创建时间：2019-03-20

### 大纲
- 第 l 章 [Spring 整体架构](#1、Spring%20整体架构)
- 第 2 章 容器的基本实现
- 第 5 章 bean 的加载
- 第 6 章容器的功能扩展
- 第 7 章 AOP


### 1、Spring 整体架构
- Core Container: Core（核心工具类）, 
	- Beans：包含访问配直文件、创建和管理 bean 以及进行 ( IoC/DI ）操作相关
	- Context 模块继承了 Beans 的特性，为 Spring 核心提供了大量扩展，添加了对国际化（如资源绑定）、事件传播、资源加载和对 Context 的透明创 建的支持。ApplicationContext 接口是 Context 模块的关键。 
	- Expression Language
- Data Access/Integration：包含JDBC、 ORM、 OXM、几础和 Transaction 模块
- Web
- AOP
- Test


### 2、容器的基本实现

#### 核心类
```java
DefaultlistableBeanFactory: 注册及加载 bean 的默认实现


XmlBeanDefinitionReader: xml配置文件读取 --> Document
	ResourceLoader：定义资源加载器，主妥应用于根据给定的资源文件地址返回对应的 Resource
	DocumentLoader：定义从资源、文件加载到转换为 Document 
```

#### 容器的基础
```java
XmlBeanFactory(Resource resourcee, BeanFactory parentBeanFactory) {
	super(parentBeanFactory);
	this.reader.loadBeanDefinitions(resource) //资源加载
}
ClassPathResource: 配置文件构造为 Resource
```

#### 解析及注册
```java
BeanDefinition (容器内部<bean/>表示)
	doRegisterBeanDefinitions(root)
		// 处理 profile
		// 解析前处理         -- 模板方法
		preProcessXml(root)
		// 解析标签
		parseBeanDefinitions(root, this.delegate)
			<bean />
			createBeanDefinition(className, parent)
			// 解析属性（scope, singleton, abstract, lazyinit, autowire, dependency-check, 
			// depends-on, autowire-candidate, primary, init-method, destroy-method, 
			// factory-method, factory-bean）
			// 解析子属性...
BeanDefinitonRegistry
	registerBeanDefinition(beanName, definitionHolder.getBeanDefinition())
	map

通知监昕器解析及注册完成
```


### 5、bean 的加载
结束了对 XML 文件的解析，接下来就是对 bean 加载的探索

```java
doGetBean(name)
	getSingleton(beanName)
		// bean 实例化
		getObjectForBeanInstance()
		// 循环依赖
		BeanFactory parentBeanFactory = getParentBeanFactory()

	// 6. 将存储 XML 配置文件的 GernericBeanDefinition 转换为 RootBeanDefinition 
```

##### FactoryBean


