

- 常用注解
```
@Aspect
@Pointcut
@Before
@After / @AfterReturning /  @AfterThrowing
@Around
@Order
```

JDK动态代理
拦截器拦截切点方法
增强与源代码编织
MethodBeforeAdvice
AfterReturningAdvice

```java
@Aspect
@Pointcut("execution()")
public void pointcut()
@Before(value = "pointcut()")
JointPoint getArgs() getSignature().getName()
@AfterReturning(value = "", returning = "result")
```

### AOP原理-@EnableAspectJAutoProxy
```
@Import({AspectJAutoProxyRegistrar.class})
	注册 AnnotationAwareAspectJAutoProxyCreator
		// Register bean processors that intercept bean creation.
		registerBeanPostProcessors(beanFactory);

```
创建AOP代理

创建对象：postProcessAfterInitialization

#### 获取拦截器链-MethodInterceptor