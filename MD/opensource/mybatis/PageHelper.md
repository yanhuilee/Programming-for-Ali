> 维护人员：**Lee**  
> 创建时间：2018-06-04

[pagehelper-spring-boot-starter](https://pagehelper.github.io/)

#### 源码说明：

分页插件项目中的正式代码一共有个6个Java文件，这6个文件的说明如下：

- Page<E>[必须]：分页参数类，该类继承ArrayList，虽然分页查询返回的结果实际类型是Page<E>,但是可以完全不出现所有的代码中，可以直接当成List使用。如果需要用到分页信息，使用下面的PageInfo类对List进行包装即可。

- PageHelper[必须]：分页插件拦截器类，对Mybatis的拦截在这个类中实现。

- PageInfo[可选]：Page<E>的包装类，包含了全面的分页属性信息。

- SqlParser[必须]：提供高效的count查询sql。主要是智能替换原sql语句为count(*)，去除不带参数的order by语句。需要jsqlparser-0.9.1.jar支持
- SqlUtil[必须]：分页插件工具类，分页插件逻辑类，分页插件的主要实现方法都在这个类中

#### 在Mybatis配置xml中配置拦截器插件:

```xml
<plugins>
    <plugin interceptor="com.github.pagehelper.PageHelper">
        <property name="dialect" value="mysql"/>

        <!-- 默认为false，设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用 -->
        <!-- 和startPage中的pageNum效果一样-->
        <property name="offsetAsPageNum" value="true"/>

        <!-- 该参数默认为false，设置为true时，使用RowBounds分页会进行count查询 -->
        <property name="rowBoundsWithCount" value="true"/>

        <!-- 设置为true时，如果pageSize=0 或 RowBounds.limit = 0 就会查询出全部的结果 -->
        <!-- （相当于没有执行分页查询，但是返回结果仍然是Page类型）-->
        <property name="pageSizeZero" value="true"/>

        <!-- 3.3.0版本可用 - 分页参数合理化，默认false -->
        <!-- 启用合理化时，如果 pageNum<1 会查询第一页，如果 pageNum>pages 会查询最后一页 -->
        <!-- 禁用合理化时，如果 pageNum<1 或 pageNum>pages会返回空数据 -->
        <property name="reasonable" value="true"/>

        <!-- 3.5.0版本可用 - 为了支持startPage(Object params)方法 -->
        <!-- 增加了一个`params`参数来配置参数映射，用于从Map或ServletRequest中取值 -->
        <!-- 可以配置pageNum,pageSize,count,pageSizeZero,reasonable,不配置映射的用默认值 -->
        <property name="params" value="pageNum=start;pageSize=limit;pageSizeZero=zero;reasonable=heli;count=contsql"/>
    </plugin>
</plugins>
```

#### spring的属性配置方式，可以使用plugins属性像下面这样配置：

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource"/>
  <property name="mapperLocations">
    <array>
      <value>classpath:mapper/*.xml</value>
    </array>
  </property>
  <property name="typeAliasesPackage" value=""/>

  <property name="plugins">
    <array>
      <bean class="com.github.pagehelper.PageHelper">
        <property name="properties">
          <value>
            dialect=hsqldb
            reasonable=true
          </value>
        </property>
      </bean>
    </array>
  </property>
</bean>
```

#### 在代码中使用

```java
// 第一种，RowBounds方式的调用
List<Country> list = sqlSession.selectList("x.y.selectIf", null, new RowBounds(1, 10));

// 第二种，Mapper接口方式的调用，推荐
PageHelper.startPage(1, 10);
List<Country> list = countryMapper.selectIf(1);
```

- 用PageInfo对结果进行包装

```java
PageInfo page = new PageInfo(list)
    getPageNum()
    getPageSize()
    getStartRow() getEndRow()
    // 分页
    getTotal()
    getPages()
    getFirstPage()
    getLastPage()
    isFirstPage()
    isLastPage()
    isHasPreviousPage()
    isHasNextPage()
```
