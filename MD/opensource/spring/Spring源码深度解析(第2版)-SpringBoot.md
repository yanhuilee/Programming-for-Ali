> 维护人员：**Lee**  
> 创建时间：2018-10-03

### Spring Boot 体系原理

#### Spring Boot 4大核心特性
自动配置、起步依赖、Actuator、命令行界面(CLI)

常用 starter
```
spart-boot-starter-activemq
spart-boot-starter-data-redis
spart-boot-starter-thymeleaf
spart-boot-starter-webflux
```

#### SpringApplication.run()

自动配置原理：
```java
@RestController
@RequestMapping
@SpringBootApplication
	//@EnableAutoConfiguration：启用Spring Boot的自动配置机制
	//@ComponentScan：@Component在应用程序所在的程序包上启用扫描
	//@Configuration：允许在上下文中注册额外的bean或导入其他配置类
```

- 66 动手实现自己的自动配置
- 67 如何在低版本 Spring 中快速实现类似自动配置的功能
```
BeanFactoryPostProcessor
```

- 70深挖 Spring Boot 的配置加载机制
```java
// 配置类
@Component
@PropertySource({"classpath:resource.properties"})
@ConfigurationProperties(profix="")

@Value("${}")
```

- 84-如何将SpringBoot应用打包成Docker镜像文件.hd

##### Jackson返回结果处理
```java
@JsonIgnore //指定字段不返回
@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss", locale="zh", timezone="GMT+8")
@JsonInclude(JsonInclude.Include.NON_NULL) //空字段不返回
@JsonProperty  //指定别名
```

对象 和 Json 的互转
```
ObjectMapper objectMapper = new ObjectMapper();
String userJsonStr = objectMapper.writeValueAsString(user);
User jsonUser = objectMapper.readValue(userJsonStr, User.class);
```

##### 12：文件上传
```
MultipartFile
    getOriginalFileName()
    transferTo(path + filename)
```
FileCopyUtils

##### 14：Dev-tool热部署
```
spring.devtools.restart.exclude=static/**,public/**
// 应用重启触发文件
spring.devtools.restart.trigger-file=xxx
```

##### 18：测试之 MockMvc
```
@RunWith(SpringRunner.class)
@SpringBootTest(class=启动类.class) //启动工程
@AutoConfigureMockMvc
```

##### 20：配置全局异常
```
// 异常注解，@RestControllerAdvice
@ControllerAdvice
// 捕获方法
@ExceptionHandler(value=Exception.class)
```


##### 22：部署war项目到tomcat
```
<build>
    <finalName>
// 启动类
A extends SpringBootServiceInitializer
protected SpringApplicationBuilder configure(SpringApplicationBuilder s) {
  return s.sources(A.class)
}
```

##### 24：自定义Filter、Listener
- 过滤器基于回调（doFilter()），依赖Servlet容器
- 过滤器是在请求进入容器后，进入servlet之前进行预处理的
- 过滤非法请求，参数，非法字符

- 拦截器基于反射（AOP），可以访问action上下文、值栈里的对象
- 拦截器希望在方法前后做些什么事情

- 处理过程：过滤前 -> 拦截前 -> action执行 -> 拦截后 -> 过滤后

![Tomcat容器](https://images2017.cnblogs.com/blog/330611/201710/330611-20171023144517066-24770749.png)

默认Filter
```
CharacterEncodingFilter
HiddenHttpMethodFilter
HttpPutFormContentFilter
RequestContextFilter
```

Filter优先级 Ordered.
```java
@WebFilter(urlPatterns = "/api/*", filterName = "LoginFilter")
class LoginFilter implements Filter {
    doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        req.getParameter("username")
        filterChain.doFilter(servletRequest, servletResponse)
  }
}
```

Servlet3
```java
@WebServlet(name = "userServlet", urlPatterns = "/customs")
class UserServlet extends HttpServlet {
  doGet()
}

@ServletComponentScan
```

常用监听器(ServletContextListener/HttpSessionListener/ServletRequestListener)
```java
@WebListener
class RequestListener implements ServletRequestListener {
    requestDestroyed(ServletRequestEvent sre) {

    }

    requestInitialized(ServletRequestEvent sre)
}
```

拦截器
```java
@Configuration
class LoginInterceptor implements HandlerInterceptor {
    preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        String token = req.getParemeter("access_token")
    }
    postHandle()
    afterCompletion()
}
```

##### 44：日志框架LogBack
slf4j

logback-spring.xml
```

```

##### 59：多环境配置
config/application-test.properties
```
spring.profile.active=test
```

##### 60：响应式编程 webflux
事件驱动-异步，非阻塞-观察者模式-通知

##### 65：服务端主动推送SSE
聊天，股票

##### 69：监控Actuator
nohup java -jar xx.jar &


---
##### Using Spring Boot without the Parent POM
```
<dependencyManagement>
	<dependencies>
		<dependency>
			<!-- Import dependency management from Spring Boot -->
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-dependencies</artifactId>
			<version>2.0.4.RELEASE</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```
