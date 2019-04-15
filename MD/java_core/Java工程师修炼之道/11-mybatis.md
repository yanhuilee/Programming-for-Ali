#####　什么是 MyBatis ？
MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJOs(Plain Old Java Objects,普通的 Java对象)映射成数据库中的记录。

```
String resource = "org/mybatis/example/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

Mybatis执行批量插入，能返回数据库主键列表吗？last_insert_id

### 整体架构
1. 接口层：SqlSession
2. 核心处理层：配置解析，参数映射，SQL解析，SQL执行，结果集映射，插件
3. 基础支持层：数据源，事务管理，缓存模块，Binding模块，反射，类型转换，日志，资源加载，解析器

### 主要核心部件
```
SqlSession：和数据库交互

Executor：sql生成，和查询缓存，事务管理

StatementHandler：封装JDBC Statement

ParameterHandler：参数

ResultSetHandler

TypeHandler：java类型和jdbc数据类型映射和转换

MappedStatement：Mapper文件 <select..>

SqlSource：将parameterObj，动态生成sql，封装到BoundSql对象

BoundSql：动态sql及参数

Configuration
```

https://baomidou.gitee.io/mybatis-plus-doc/#/

#### Mybatis管理事务是分为两种方式:

(1)使用JDBC的事务管理机制,就是利用java.sql.Connection对象完成对事务的提交

(2)使用MANAGED的事务管理机制，这种机制mybatis自身不会去实现事务管理，而是委托容器（Spring）来实现对事务的管理，对事务提交和回滚并不会做任何操作

```
<!--配置事务的管理方式-->  
<transactionManager type="JDBC" />  
```

Mybatis提供了一个事务接口Transaction，以及两个实现类 `JdbcTransaction` 和 `ManagedTransaction`，当spring与Mybatis一起使用时，spring提供了一个实现类 `SpringManagedTransaction`
```java
// 事务接口  
interface Transaction {  
  // 获得数据库连接  
  Connection getConnection() throws SQLException;  

  void commit() throws SQLException;  

  void rollback() throws SQLException;  

  void close() throws SQLException;  
```

##### 事务对SELECT的影响
事务对select操作的影响主要体现在对缓存的影响上，主要包括一级缓存和二级缓存
- 因为一级缓存是Session级别的，事务的提交回滚对MyBatis的一级缓存没有影响；一级缓存放在BaseExector中的PerpectualCache类型的localCache中；

##### 总结
MyBatis可以通过XML配置是否独立处理事务，可以选择不单独处理事务，将事务托管给其他上层框架如spring等；

如果MyBatis选择处理事务则事务会对数据库操作产生影响
- 对UPDATE操作的影响主要表现在UPDATE操作后如果没有进行事务提交则会子啊会话关闭时进行数据库回滚；

- 对SELECT操作的影响主要表现在二级缓存上，执行SELECT操作后如果未进行事务提交则缓存会被放在临时缓存中，后续的SELECT操作无法使用该缓存，直到进行commit()事务提交，临时缓存中的数据才会被转移到正式缓存中；
