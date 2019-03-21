### SpringMVC
> 原理：通过 servlet 拦截所有 URL 来达到控制的目的

```java
ContextLoaderListener
	自动装配 ApplicationContext 信息（WebApplicationContext）

DispatcherServlet： 拦截 web 请求
	init(): 九大组件
		HandlerMappings
		HandlerAdapters
		ViewResolvers
	service()

ModeAndView
SimpleUrlHandlerMapping
```