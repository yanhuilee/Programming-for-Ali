
Spring 的常用组件
- Spring 必需的组件，包括 Bean 容器、 Context 上下文支持、 Spring EL 以及相关支持工具类库。 这些是其他所有组件依赖的基础。
- Spring AOP 是做日志统一管理、事务管理时需要用到的组件
- SpringORM 中提供了对 Hibernate 等 ORM 框架的整合组件
- Spring JDBC 的 JdbcTemplate 是在做数据库操作时经常用到的组件
- Spring TX 提供了数据库事务管理相关的组件
- Spring Web 提供了 Web 开发时用到的诸如 Web 工具、视图解析器等组件。 其通常 与 Spring Web MVC 一起使用

以上是 Spring框架的核心组成， 官方称之为 Spring Framework。 除此之外，还有几个 Spring 项目也比较常用。 
- Spring Data：包括 Spring Data Redis、 Spring Data MongoDB、 Spring Data Solr 等， 它们是对很多数据存储软件的操作封装。 
- Spring Boot： 是 Spring所有组件的集合，旨在简化 Spring 应用的配置和降低 Spring 使用的复杂度。
- Spring Security： 提供了权限管理、 OAuth 开发等基础组件

### 1、Spring 核心组件
Spring 框架的核心包括 IoC、 AOP 以及辅助工具 SpringEL 等。

首先要明确一个概念， Spring 的 IoC 容器 是 ApplicationContext。 常用的 ApplicationContext 如下:
- ClassPathXmlApplicationContext：从 ClassPath 路径中加载 XML 配置的上下文
- FileSystemXmlApplicationContext：从文件系统中加载
- XmlWebApplicationContext: Web 开发中从 XML 中记载 Web 上下文，区别于上面 两个之处在于， 此上下文是基于 ServletContext 的
- AnnotationCon句WebApplicationContext：从注解类中加载 Web 上下文。

这些上下文中的实例在 Spring 中被叫作 Bean。 一个 Bean 的生命周期管理如图 4-3所示s

#### 1. Spring 的双亲上下文机制

Spring 中的 ApplicaitonContext 是父子层次结构的，在存在多个上下文的时候，会有一个根上下文作为其他上下文的"父亲"：
```xml
<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class> 
</listener>
```

使用 Listener 监听器来加载 Spring 的配置，Spring 会创建一个全局的 WebApplicationContext 上下文，其被称为根上下文，保存在 ServletContext 中，key 是 `WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE` 属性的值。

而在很多情况下，我们还会配置一个或者多个 DispatcherServlet，每个 DispatcherServlet 都有一个自己的 WebApplicationContext 上下文。 这个上下文是私有的， 继承了根上下文中的所有东西。 该上下文保存在 ServletContext 中， key 是 `org.springframework.web. servlet.FrameworkServlet.CONTEXT + Servlet 名称`。 当一个 Request 对象产生时，会把这个 WebApplicationContext 上下文保存在 Request 对象中， key 是 `DispatcherServlet.class.
getName() ＋ ".CONTEXT"`。 

为避免双亲上下文，可以不使用 Listener 监昕器加载 Spring 的配置， 直接改用 DispatcherServlet 加载 Spring 的配置。


#### 2. Spring 中的事件机制

Spring 中提供了事件机制，用于监昕容器事件的发生，在事件发生时做一些处理工作。

1)  **容器事件监昕器**： 实现 ApplicationListener 接口的类，可以监昕容器的事件，包括 如下几种:
- ContextStartedEvent：上下文启动事件
- ContextRefreshedEvent： 上下文刷新完毕
- ContextStoppedEvent： 上下文停止
- ContextClosedEvent： 上下文关闭

```java
@Service 
public class TestListener implements ApplicationListener<ContextStartedEvent> { 
	@Override public void onApplicationEvent(ContextStartedEvent event) { 
	}
}

<bean class＝"xx.TestListener" scope＝"prototype" />
```

2) **具有意识的 Bean**： 实现了形如 xxAware 接口的 Bean，能够注入/获取 xx代表的事物。 如，实现 ApplicationContextAware 接口的类，会自动注入当前的 ApplicationContext。

此外 BeanNameAware 和 BeanFactoryAware
- 实现了 BeanNameAware 的 Bean 能够感知到 自己在 BeanFactory 中注册的名称
- 实现了 BeanFactoryAware 的 Bean 能够感知到 自己所属的 BeanFactory


#### 3. Bean 的初始化和销毁

如果想要在应用初始化的时候做一些初始化方法，可以使用以下几种初始化方式。
- 使用＠PostConstruct 注解，指明在 Bean 构造器方法执行后执行的方法。
- Bean 实现 InitializingBean 接口，在 afterPropertiesSet 中做初始化工作。
- init-method 指定 Bean 构造完成后调用的方法

如果想在某些 Bean 初始化完毕并注入进来之后再进行初始化工作，可以 配合使用＠DependsOn

也可以使用 BeanFactoryPostProcessor 和 BeanPostProcessor 来做一些更前置的初始化工作，典型的应用场景就是实现自己的注解。 

- 要实现BeanFactoryPostProcessor接口 ，可以在 Spring容器加载了 Bean 的定义之后，在 Bean 实例化之前执行，这样能够修改 Bean 的定义属性。 如可以把 Bean 的 scope 从 singleton 改为 prototype，也可以把 property 的值修改掉。 可以通过接口的参数获 取相关 Bean 的定义信息。 

- 实现 BeanPostProcessor 接口可以在 Spring 容器实例化 Bean 之后，在执行 Bean 的初始化方法前后，添加一些向己的处理逻辑。

Spring内置了几个BeanPostProcessor实现：
- CommonAnnotationBeanPostProcessor： 支持＠Resource 注解的注入
- RequiredAnnotationBeanPostProcessor： 支持＠Required 注解的注入
- AutowiredAnnotationBeanPostProcessor： 支持＠Autowired 注解的注入
- ApplicationContextAwareProcessor：用来为 Bean 注入 ApplicationContext 等容器对象

根据 Bean 的生命周期。 以上提到的初始化方法的优先级为： BeanFactoryPostProcessor
> Constructor > BeanPostProcessor. postProcessBeforelnitialization > @PostConstruct > 
InitializingBean > init-method。 

此外，如果想要在所有 Bean 都初始化完毕后做一次初始化工作，那么可以使用 4.1.2 节所介绍的 ApplicationListener，监昕 ContextRefreshedEvent:
```java
@Service 
public class BootstrapService implements ApplicationListener<ContextRefre shedEvent> { 
	@Override public void onApplicat工onEvent (ContextRefreshedEvent event) {
		// 初始化代码
	}
}
```

要在销毁 Bean 之前做一些收尾工作，有以下 3 种方式。
- 使用＠PreDestroy， 指明在容器关闭后执行的方法
- 实现 DisposableBean 接口，在 destroy() 中做销毁工作
- 在 XML 中使用 destroy-method 指定 Bean 销毁时调用的方法

根据 Bean 的生命周期，可知优先级为：＠PreDestroy > DisposableBean > destroymethod


#### 4. Bean 的动态构造
当要创建的 Bean 不能直接通过构造方法、 setter 方法、字段注入完成，还需要做一些初始化工作的时候，普通创建 Bean 的方式就力不从心了。 Spring 提供了 3 种方式解决这个问题。

1) 定义 Bean 时，指明 factory-bean 和 factory-method
```java
public class TestFactory{ 
	public User getUser() { return new User();}
} 

<bean id="user" class="xx.User" factory-bean="testFactory" factory-method="getUser"  />
```

2) 定义 Bean 直接使用类的静态方法
```java
public class TestFactory{ 
	public static User getStaticUser() { return new User()}
}

<bean id="user" class="xx.TestFactory" factory-method="getStaticUser" /> 
```

3) 实现 FactoryBean 接口
```java
public interface FactoryBean<T> { 
	T getObject () throws Exception;
	Class<?> getObjectType();
	boolean isSingleton();
}

public class TestFactory implements FactoryBean<User> {
	@Override
	public User getObject() throws Exception { 
		return new User ("test" );
	}
	@Override
	public Class<?> getObjectType() { 
		return User.class;
	} 
	@Override public boolean isSingleton() { return true; }
}

<bean id= "user" class="xx.TestFactory" /> 
```

> P144

现在 Spring 提供了＠Bean 注解，完全可以在代码中做这种动态生成 Bean 的工作：
```java
@Configuration
public class SpringCoreConfig {
	@Bean ("testUser") 
	public User TestUser(AdminUser adminUser)｛
		// 参数可以直接引用 Bean
		User user = new User();
		user.setUsername(adminUser.getUser().getUsername());
		return user;
	}
}
```

#### 5. 注入集合、枚举、类的静态字段

#### 6. 面向切面编程－AOP
AOP ( Aspect Oriented Programming ）是为了解决某些场景下代码重复问题的一种编程技术，允许程序模块化横向切割关注点或横向切割典型的责任划分。其能够封装多个类中不同单元的相同功能，把应用业务逻辑和系统服务分开，经常用在日志和事务管理上。

说到 AOP 不得不提的就是 AspectJ 这个 AOP 框架，它是一种编译期 AOP 框架（即在编译的时候对被代理的类进行增强） 。 它自己有一套 AOP 各个组件的概念定义。 

Spring AOP 是一种运行期的 AOP 框架，对 AspectJ 的支持只是使用了其定义的各个组件的定义和注解，底层实现和它并没有多少关系。

使用 SpringAOP 有如下 3 种方式：

- 1) Spring AOP 接口 。

这种方式基于 Spring 提供的 AOP 接口
```
前置通知： MethodBeforeAdvice
后置通知： AfterReturningAdvice
环绕通知： Methodlnterceptor
异常通知： ThrowsAdvice 
```
针对这些接口，进行实现并配置即可。 


#### 8. 无 XML 的配置方式
常用注解
```
＠Configuration
@Bean
@Profile
@ComponentScan
@EnableWebMVC
@Import
@PropertySource：配合＠Configuration
```


### 2、Spring 数据操作框架
Spring 提供了对常用数据软件／服务操作的封装组件，包括关系型数据库、 NoSQL 数据库、 Redis、 Elasticsearch、 Cassandra 等。 本章主要讲述其中最常用的 JDBC、 Redis 以及 MongoDB。

#### 1. Spring JDBC

比较常用的是 Spring ORM 下面一层的 Spring JDBC 和 Spring TX。 

1) Spring JDBC 提供了对 JDBC 操作的封装，也是 Java 开发中经常用到的数据库操作工具，经常在需要灵活组装 SQL 的场景下使用，其核心类是 JdbcTemplate，提供了很多数 据 CRUD 操作

2) Spring .TX 提供了对事务的支持。 使用 tx:annotation-driven 开启 Spring 的注解事务，并配置 transaction-manager

使用＠Transactional 注解一个方法使其开启事务 


#### 2. Spring Data Redis
```
1）配置 Jedis 连接池: JedisPoolConfig
2）配置连接工厂: . JedisConnectionFactory
3) 构造 RedisTemplate 即可使用它来做各种操作
	redisTemplate.opsForHash().put("testKey", "testField", "testValue"); //hset
```

需要注意的是，如果直接使用 RedisTemplate，那么 Redis 的 key使用的是 String 的 JDK 序列化字节数组，并非是用 String伊tBytes（） 得到的字节数组。 可以通过 RedisTemplate 的defaultSerializer、 keySerializer、 valueSerializer、 hashKeySerializer 以及 hashValueSerializer 几 个属性设置想要使用的序列化机制。 其支持的几种序列化机制如下。

- StringRedisSerializer： 简单的字符串序列 化，使用的 是 String.getBytes（）方法。 StringRedisTemplate 就是使用它作为 k町、 value、 hashKey 以及 hashValue 的序列化 实现。 - GenericToStringSerializer：可以将任何对象泛化为字符串并序列化，对于每一种对 象类型都有不同的实现。
- JacksonJsonRedisSerializer: JSON 的序列化方式，使用 Jakson Mapper 将 object 序列 化为 JSON 字符串。
- Jackson2JsonRedisSerializer：跟 JacksonJsonRedisSerializer 同样是 JSON 序列化方式， 使用的是 jackson databind。
- JdkSerializationRedisSerializer： 使用 JDK 自带的序列化机制，也是直接使用 RedisTemplate 时用的序列化机制


### 3、Spring Boot
本章开始提到过 Spring 现在变得越来越复杂，这一点 Spring Source 自己也注意到了，因此推出了 Spring Boot，旨在降低使用 Spring 的门槛。 其大大简化了 Spring 的配置工作，并且能够很容易地将应用打包为可独立运行的程序（即不依赖于第三方容器，可以独立以 jar 或者 war 包的形式运行）。 其带来的开发效率的提升使得 Spring Boot 被看作至少近 5 年来 Spring 乃至整个 Java 社区最有影响力的项目之一，也被人看作 Java EE 开发的颠覆者。 另一方面来说， Spring Boot 也顺应了现在微服务（ MicroService ）的理念，可以用来构建基于 Spring 框架的可独立部署应用程序。

#### 2. Spring Boot 的运行原理
@SpringBootApplication

##### Spring Boot 的组成模块
```
spring-boot-starter-web
spring-boot-starter-logging 开启 SLF4J 和 Logback 日志支持
spring-boot-starter-redis
spring-boot-starter-jdbc
```

### 4、Spring 常用组件
#### 1. 表达式引擎－Spring Expression Language
#### 2. 远程过程访问的支持－Spring Remoting
#### 3. Spring 与 JMX 的集成
#### 4. 定时任务的支持－Spring Quartz
#### 5. 跨域请求的支持－Spring CORS
