```java
# 配置
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.url=
mybatis.config-location=classpath:/
mybatis.mapper-locations=classpath*:mapper/**/*.xml

@MapperScan(basePackages=)
@Mapper
public interface CityMapper {
    @Select("SELECT * FROM CITY WHERE state = #{state}")
    City findByState(@Param("state") String state);
}

@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

### 整合 MyBatis
MyBatis 中的配置文件主要封装在 configuration

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	<property name="configLocation" value="classpath:test/mybatis/MyBatis-Configuration.xml"></property>
	<property name="dataSource" ref="dataSource" />
</bean>
<bean id="userMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
	<property name="rnapperinterface" value="test.mybatis.dao.UserMapper"></property>
	<property name= "sqlSessionFactory" ref="sqlSessionFactory"></property>
</bean> 
```

```java
sqlSessionFactory.openSession().getMapper(XxMapper.class)

or

(XxMapper) context.getBean("XxMapper")
```

##### 源码解析

- sqlSessionFactory 创建
```java
InitializingBean: afterPropertiesSet() //进行bean的逻辑初始化
	// SqlSessionFactory初始化
	this.sqlSessionFactory = buildSqlSessionFactory()
		plugins
FactoryBean: getObject() //返回实例
	return this.sqlSessionFactory
```

- MapperFactoryBean 创建
```java
afterPropertiesSet()
	// dao 配置验证（接口和文件存在性）
	checkDaoConfig()
		configuration = getSqlSession().getConfiguration()
		configuration.addMapper(this.mapperInterface)
	initDao()
getObject()
	return getSqlSession().getMapper(this.mapperInterface)
```

- MapperScannerConfigurer 扫描包
```xml
# 获取名称
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	<property name="basePackage" value="test.mybatis.dao" />
</bean>
```
```java
InitializingBean
BeanDefinitionRegistryPostProcessor
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
    if (this.processPropertyPlaceHolders) {
        this.processPropertyPlaceHolders();
    }

    ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
    scanner.setAddToConfig(this.addToConfig);
    scanner.setAnnotationClass(this.annotationClass);
    scanner.setMarkerInterface(this.markerInterface);
    scanner.setSqlSessionFactory(this.sqlSessionFactory);
    scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
    scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
    scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
    scanner.setResourceLoader(this.applicationContext);
    scanner.setBeanNameGenerator(this.nameGenerator);
    scanner.registerFilters();
    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n"));
, ApplicationContextAware, BeanNameAware
```
