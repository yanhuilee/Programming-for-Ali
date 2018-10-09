```
mybatis-spring
@MapperScan
@ComponentScan
```

#### 原理：代理 MapperProxy
```
SqlSession = DefaultSqlSessionFactory.openSession()
sqlSession.selectOne("namespace.methonId", ) --> selectList()
xxMapper = sqlSession.getMapper(xxMapper.class)
<String, MappedStatement>
CachingExecutor
BoundSql
```

分页：RowBounds
结果映射：resultMap
