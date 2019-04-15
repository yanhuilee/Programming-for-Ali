> 创建时间：2019-03-20

### Spring 整体架构
- Core Container: Core, Beans, Context 和 Expression Language
- Data Access/Integration
- Web
- AOP
- Test

### 2、容器的基本实现

核心类
```java
DefaultlistableBeanFactory: 注册及加载 bean 的默认实现
	XmlBeanFactory: 容器的基础

XmlBeanDefinitionReader: xml配置文件读取 --> Document
```

```java
解析及注册 BeanDefinition (容器内部<bean>表示)
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

### bean 的加载
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


### 事务
```java
@Transactional(propagation=Propagation.REQUIRED)

DataSourceTransactionManager

TxNamespaceHandler <tx:annotation-dirven />
	init()
		registerBeanDefinitionParser("annotation-dirven", new AnnotationDrivenBeanDifinitionParser())
			parse(Element element, ParserContext parserContext) {
				// Spring 中的事务是以 AOP 基础的
				if ("aspectj".equals(element.getAttribute("mode")))
			}
```

##### 事务增强器
- 创建事务
- 回滚处理
- 事务提交

### Spring Boot 体系原理
##### SpringApplication 启动
```java
// 创建
context = createApplicationContext()
refreshContext(context)
afterRefresh(context, applicationArguments)
```