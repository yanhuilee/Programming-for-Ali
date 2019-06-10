### 一、容器相关

##### 组件注册
```
@Configuration @Bean("name")  //配置类
@ComponentScan(value = "要扫描的包")
// @Service、@Repository、@Component、@Controller，@Configuration
```

- @Scope：设置组件作用域
```
prototype：多实例，每次获取时都调用方法创建对象
singleton：启动时创建，map.get()获取
request：每次请求创建一个实例
session：
// 单例类引用prototype 问题：单例类只实例化一次，所以属性也只设置一次
```

```
@Lazy

@Import(xx.class) //快速导入组件
	ImportSelector
```

AnnotationConfigApplicationContext(配置类.class)

FactoryBean<xx> 注册组件
```
public interface FactoryBean<T> {
    // & 返回工厂bean本身
    T getObject() throws Exception; // return new xx()
    Class<?> getObjectType();       // return xx.class
    boolean isSingleton();
}
```

##### 属性赋值
- @Value赋值  ${}
```
@Component
@ConfigurationProperties(prefix = "book")
```

- @PropertySource加载外部配置文件
```
作用相当于
<context:property-placeholder location="" />
```

##### 自动装配

- @Autowired&@Qualifier&@Primary
```
@Autowired(required = false) //参数，方法，构造器的自动装配
	setXx()

Aware
    ApplicationContextAware  // 注入IoC容器
    BeanNameAware
```

- 根据环境注册bean
```
@Profile("default")
-Dspring.profiles.active=

// 获取环境
applicationContext.getEnviroment()
```

##### 生命周期
- 初始化：对象创建完成，赋值，调用初始化方法
- 销毁：
	- 单实例：容器关闭时
	- 多实例：容器不会管理这个bean，不会调用销毁方法

```java
方式一： @Bean(initMethod = "init", destroyMethod = "destroy") //指定初始化和销毁方法
方式二： InitializingBean 和 DisposableBean
方式三：
// bean 创建完，属性赋值之后
@PostConstruct
方式四： 
BeanPostProcessor ////bean构造之后，初始化前后进行处理工作
```

### AOP
```
28、[源码]-AOP原理-@EnableAspectJAutoProxy
30、[源码]-AOP原理-注册AnnotationAwareAspectJAutoProxyCreator
32、[源码]-AOP原理-创建AOP代理
33、[源码]-AOP原理-获取拦截器链-MethodInterceptor
34、[源码]-AOP原理-链式调用通知方法
```


### 划重点
aware beanpostprocessor refresh()


### servlet3

HttpServlet
@WebServlet("/xx")
@WebFilter

- ServletContainerInitializer

ServletContext 注册 web组件：servlet/filter/listener
```
onStartUp(ServletContext s) {
	s.addServlet("", obj)
}
```


#### 异步请求