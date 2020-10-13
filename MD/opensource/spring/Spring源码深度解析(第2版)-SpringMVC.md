```
DispatcherServlet
	Controller
	xxResolver
		ViewResolver、HandlerExceptionResolver、MultipartResolver 
	HandlerMapping

请求处理机制：⼀个请求的⼤大致处理理流程
	绑定⼀些 Attribute
		WebApplicationContext / LocaleResolver / ThemeResolver
	处理 Multipart
		如果是，则将请求转为 MultipartHttpServletRequest
	Handler 处理
		如果找到对应 Handler，执行 Controller 及前后置处理理器逻辑
	处理返回的 Model ，呈现视图

定义映射关系
	@Controller
	@RequestMapping
		path / method  指定映射路径与⽅法
		params / headers 限定映射范围
		consumes / produces 限定请求与响应格式
	一些快捷方式
		@RestController 
		@GetMapping / @PostMappin
定义处理方法：
	@RequestBody / @ResponseBody / @ResponseStatus(HttpStatus.CREATED)
	@PathVariable / @RequestParam / @RequestHeader
	HttpEntity / ResponseEntity 
	BindingResult
	ModelMap
定义类型转换：
	实现WebMvcConfigurer
	Spring Boot 在 WebMvcConfiguration 中实现了一个
	添加自定义的 Converter
	添加自定义的 Formatter

视图解析机制
	ViewResolver 与 View 接口
	Thymeleaf、FreeMarker
	ThymeleafAutoConfiguration
	ThymeleafViewResolver
	@ModelAttribute
常用视图：
静态资源和缓存
异常处理机制：
	
拦截器：
	HandlerInterceptor
		boolean preHandle()
		void postHandle()
		void afterCompletion()
	针对 @ResponseBody 和 ResponseEntity 的情况
		ResponseBodyAdvice
	针对异步请求的接口
		AsyncHandlerInterceptor
			void afterConcurrentHandlingStarted()
	拦截器的配置方式
		WebMvcConfigurer.addInterceptors()
```


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