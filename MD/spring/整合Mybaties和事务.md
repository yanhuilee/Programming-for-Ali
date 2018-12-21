
> www.mybatis.org/mybatis-3/zh/java-api.html

```
mybatis-spring-boot-starter runtime
mysql-commector-java runtime
druid

# 配置
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.url=
mybatis.config-location=classpath:/
mybatis.mapper-locations=classpath:/

@MapperScan(basePackages=)
@Mapper
public interface CityMapper {
    @Select("SELECT * FROM CITY WHERE state = #{state}")
    City findByState(@Param("state") String state);
}

@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

##### 35：事务介绍和常见的隔离级别，传播行为
Serializable, Repeatable Read, Read Committed, Read Uncommitted

事务传播行为：


事务
```java
@Transactional(propagation=Propagation.REQUIRED)
```

---
```
<!-- h2内存数据库 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```