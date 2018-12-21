BeanFactory：工厂模式
bean工厂和ApplicationContext

IoC：依赖注入，
  Bean: 作用域-prototype，生命周期（init-method/destory-method）
  自动装配：byType

注解：
  <context:annotation-config />
  @Bean @Required @Autowired @Qualifier

数据访问：
  JdbcTemplate
  支持的ORM: iBatis, JPA

声明式事务：
  配置

AOP:
  Aspect
  五种类型通知

MVC:
  DispatcherServlet: 处理所有http请求和响应
  WebApplicationContext
  @Controller
  @RequestMapping
