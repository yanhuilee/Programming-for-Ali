#### Mybatis至少遇到了以下的设计模式的使用：

1. Builder模式，例如SqlSessionFactoryBuilder、XMLConfigBuilder、XMLMapperBuilder、XMLStatementBuilder、CacheBuilder

构建复杂对象
```java
在Mybatis环境的初始化过程中，
SqlSessionFactoryBuilder
	XMLConfigBuilder 读取配置文件和映射文件，构建Mybatis运行的核心对象Configuration对象
	XMLMapperBuilde：mapper文件
	XMLStatementBuilder：sql语句
```

2. 工厂模式，例如SqlSessionFactory、ObjectFactory、MapperProxyFactory

简单工厂模式：根据参数的不同返回不同类的实例
```java
SqlSessionFactoryBuilder#builder(Configuration/InputStream)

SqlSessionFactory
	getConfiguration()
	openSession(ExecutorType, Connection)
```

DefaultSqlSessionFactory
```java
private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level,
	boolean autoCommit) {
 	Transaction tx = null;
   	try {
   		// 读取 Configuration 中环境配置
		final Environment environment = configuration.getEnvironment();
		// 初始化 TransactionFactory 获得 Transaction对象
		final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
		tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
		// 获取 Executor 对象
		final Executor executor = configuration.newExecutor(tx, execType);
		// 构建 SqlSession
		return new DefaultSqlSession(configuration, executor, autoCommit);
   	} catch (Exception e) {
 		closeTransaction(tx); // may have fetched a connection so lets call close()
     	throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
   	}
}
```

LogFactory
```java
public final class LogFactory {
	// 该工厂生产的不只是一个产品，而是具有Log公共接口的一系列产品，
	// 比如Log4jImpl、Slf4jImpl等很多具体的Log
	private static Constructor<? extends Log> logConstructor;

	private LogFactory() {}

	public static Log getLog(Class<?> aClass) {
 		return getLog(aClass.getName());
	}
}
```

3. 单例模式，例如ErrorContext和LogFactory

ErrorContext是用在每个线程范围内的单例，用于记录该线程的执行环境错误信息
```java
public class ErrorContext {
	private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<ErrorContext>();

	private ErrorContext() {}

	public static ErrorContext instance() {
		ErrorContext context = LOCAL.get();
		if (context == null) {
			context = new ErrorContext();
			LOCAL.set(context);
		}
		return context;
	}
}
```


4. 代理模式，Mybatis实现的核心，比如MapperProxy、ConnectionLogger，

用的jdk的动态代理；还有executor.loader包使用了cglib或者javassist达到延迟加载的效果

代理模式包含如下角色：
- Subject: 抽象主题角色
- Proxy: 代理主题角色
- RealSubject: 真实主题角色

```java
public class MapperProxyFactory<T> {
	private final Class<T> mapperInterface;
	private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

	public MapperProxyFactory(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}
	public Map<Method, MapperMethod> getMethodCache() {
		return methodCache;
	}

	@SuppressWarnings("unchecked")
	protected T newInstance(MapperProxy<T> mapperProxy) {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), 
			new Class[] { mapperInterface }, mapperProxy);
	}

	public T newInstance(SqlSession sqlSession) {
		final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
		return newInstance(mapperProxy);
	}
}
```

MapperProxy
```java
public class MapperProxy<T> implements InvocationHandler, Serializable {
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			if (Object.class.equals(method.getDeclaringClass())) {
				return method.invoke(this, args);
			} else if (isDefaultMethod(method)) {
				return invokeDefaultMethod(proxy, method, args);
			}
		} catch (Throwable t) {
			throw ExceptionUtil.unwrapThrowable(t);
		}
		final MapperMethod mapperMethod = cachedMapperMethod(method);
		return mapperMethod.execute(sqlSession, args);
	}
}
```

当真正执行一个Mapper接口的时候，就会转发给`MapperProxy.invoke()`，该方法则会调用后续的 `sqlSession.cud>executor.execute>prepareStatement`等一系列方法，完成SQL的执行和返回。


5. 组合模式，例如SqlNode和各个子类ChooseSqlNode

组合模式组合多个对象形成树形结构以表示“整体-部分”的结构层次（叶子对象和组合对象实现相同的接口）
```xml
<update id="update" parameterType="User">
   UPDATE users
   <trim prefix="SET" prefixOverrides=",">
       <if test="name != null and name != ''">
           name = #{name}
       </if>
   </trim>
   where id = ${id}
</update>
```

在DynamicSqlSource.getBoundSql方法里，调用了rootSqlNode.apply(context)方法，apply方法是所有的动态节点都实现的接口：
```java
interface SqlNode {
	boolean apply(DynamicContext context);
}
```

对于实现该SqlSource接口的所有节点，就是整个组合模式树的各个节点：
```java
SqlNode
	ChooseSqlNode
	ForEachSqlNode
	IfSqlNode
	MixedSqlNode
	StaticTextSqlNode
	TrimSqlNode
		SetSqlNode
		WhereSqlNode
	VarDeclSqlNode

// 组合模式的简单之处在于，所有的子节点都是同一类节点，可以递归的向下执行，
// 比如对于TextSqlNode，因为它是最底层的叶子节点，所以直接将对应的内容append到SQL语句中
@Override
public boolean apply(DynamicContext context) {
	GenericTokenParser parser = createParser(new BindingTokenParser(context, injectionFilter));
	context.appendSql(parser.parse(text));
	return true;
}

// 对于IfSqlNode，就需要先做判断，如果判断通过，仍然会调用子元素的SqlNode，
// 即contents.apply方法，实现递归的解析
@Override
public boolean apply(DynamicContext context) {
	if (evaluator.evaluateBoolean(test, context.getBindings())) {
		contents.apply(context);
		return true;
	}
	return false;
}
```


6. 模板方法模式，例如BaseExecutor和SimpleExecutor，还有BaseTypeHandler和所有的子类例如IntegerTypeHandler

模板方法模式是所有模式中最为常见的几个模式之一，是基于继承的代码复用的基本技术。模板类定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。

在Mybatis中，sqlSession的SQL执行，都是委托给Executor实现的，Executor包含以下结构：
```java
Executor
	BaseExecutor
		BatchExecutor
		SimpleExecutor
		ReuseExecutor
	CachingExecutor

// BaseExecutor就采用了模板方法模式，它实现了大部分的SQL执行逻辑，然后把以下几个方法交给子类定制化完成：
protected abstract int doUpdate(MappedStatement ms, Object parameter)

protected abstract List<BatchResult> doFlushStatements(boolean isRollback)

protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds,
     ResultHandler resultHandler, BoundSql boundSql)
```

该模板方法类有几个子类的具体实现，使用了不同的策略：

- 简单SimpleExecutor：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。（可以是Statement或PrepareStatement对象）

- 重用ReuseExecutor：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map<String, Statement>内，供下一次使用。（可以是Statement或PrepareStatement对象）

- 批量BatchExecutor：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理的；BatchExecutor相当于维护了多个桶，每个桶里都装了很多属于自己的SQL，就像苹果蓝里装了很多苹果，番茄蓝里装了很多番茄，最后，再统一倒进仓库。（可以是Statement或PrepareStatement对象）

比如在SimpleExecutor中这样实现update()：
```java
@Override
public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
    Statement stmt = null;
    try {
    	Configuration configuration = ms.getConfiguration();
    	StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null,
         null);
    	stmt = prepareStatement(handler, ms.getStatementLog());
    	return handler.update(stmt);
    } finally {
    	closeStatement(stmt);
   	}
}
```

7. 适配器模式，例如Log的Mybatis接口和它对jdbc、log4j等各种日志框架的适配实现

Mybatsi的logging包中，有一个Log接口：
```java
public interface Log {
	boolean isDebugEnabled();
	boolean isTraceEnabled();

	void error(String s, Throwable e);
	void error(String s);

	void debug(String s);
	void trace(String s);
	void warn(String s);
}
```

该接口定义了Mybatis直接使用的日志方法，而Log接口具体由谁来实现呢？Mybatis提供了多种日志框架的实现，这些实现都匹配这个Log接口所定义的接口方法，最终实现了所有外部日志框架到Mybatis日志包的适配：
```
Log
	Slf4jLoggerImpl
```

比如对于Log4jImpl的实现来说，该实现持有了org.apache.log4j.Logger的实例，然后所有的日志方法，均委托该实例来实现。
```java
public class Log4jImpl implements Log {
	private static final String FQCN = Log4jImpl.class.getName();
	private Logger log;

	public Log4jImpl(String clazz) {
		log = Logger.getLogger(clazz);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
	return log.isTraceEnabled();
	}

	@Override
	public void error(String s, Throwable e) {
		log.log(FQCN, Level.ERROR, s, e);
	}

	@Override
	public void error(String s) {
		log.log(FQCN, Level.ERROR, s, null);
	}

	@Override
	public void debug(String s) {
		log.log(FQCN, Level.DEBUG, s, null);
	}

	// ...
}
```

8. 装饰者模式，例如Cache包中的cache.decorators子包中等各个装饰者的实现

装饰模式(Decorator Pattern) ：动态地给一个对象增加一些额外的职责(Responsibility)，就增加对象功能来说，装饰模式比生成子类实现更为灵活。

Cache（org.apache.ibatis.cache.Cache）整个体系采用装饰器设计模式，数据存储和缓存的基本功能由PerpetualCache（org.apache.ibatis.cache.impl.PerpetualCache）永久缓存实现，然后通过一系列的装饰器来对PerpetualCache永久缓存进行缓存策略等方便的控制。如下图：
```
Cache
	PerpetualCache
	Package decorator
```

用于装饰PerpetualCache的标准装饰器共有8个（全部在org.apache.ibatis.cache.decorators包中）：

- FifoCache：先进先出算法，缓存回收策略
- LoggingCache：输出缓存命中的日志信息
- LruCache：最近最少使用算法，缓存回收策略
- ScheduledCache：调度缓存，负责定时清空缓存
- SerializedCache：缓存序列化和反序列化存储
- SoftCache：基于软引用实现的缓存管理策略
- SynchronizedCache：同步的缓存装饰器，用于防止多线程并发访问
- WeakCache：基于弱引用实现的缓存管理策略

另外，还有一个特殊的装饰器TransactionalCache：事务性的缓存

正如大多数持久层框架一样，mybatis缓存同样分为一级缓存和二级缓存

1. 一级缓存，又叫本地缓存，是PerpetualCache类型的永久缓存，保存在执行器中（BaseExecutor），而执行器又在SqlSession（DefaultSqlSession）中，所以一级缓存的生命周期与SqlSession是相同的。

2. 二级缓存，又叫自定义缓存，实现了Cache接口的类都可以作为二级缓存，所以可配置如encache等的第三方缓存。二级缓存以namespace名称空间为其唯一标识，被保存在Configuration核心配置对象中。

二级缓存对象的默认类型为PerpetualCache，如果配置的缓存是默认类型，则mybatis会根据配置自动追加一系列装饰器。

Cache对象之间的引用顺序为：

SynchronizedCache–>LoggingCache–>SerializedCache–>ScheduledCache–>LruCache–>PerpetualCache


9. 迭代器模式，例如迭代器模式PropertyTokenizer

> GOF给出的定义为：提供一种方法访问一个容器（container）对象中各个元素，而又不需暴露该对象的内部细节

Mybatis的PropertyTokenizer是property包中的重量级类，该类会被reflection包中其他的类频繁的引用到。这个类实现了Iterator接口，在使用时经常被用到的是Iterator接口中的hasNext()
```java
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {
	private String name;
	private String indexedName;
	private String index;
	private String children;

	public PropertyTokenizer(String fullname) {
		int delim = fullname.indexOf('.');
		if (delim > -1) {
			name = fullname.substring(0, delim);
			children = fullname.substring(delim + 1);
		} else {
			name = fullname;
			children = null;
		}
		indexedName = name;
		delim = name.indexOf('[');
		if (delim > -1) {
			index = name.substring(delim + 1, name.length() - 1);
			name = name.substring(0, delim);
		}
	}

	// getter..

	@Override
	public boolean hasNext() {
		return children != null;
	}

	@Override
	public PropertyTokenizer next() {
		return new PropertyTokenizer(children);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException(
			"Remove is not supported, as it has no meaning in the context of properties.");
	}
}
```

可以看到，这个类传入一个字符串到构造函数，然后提供了iterator方法对解析后的子串进行遍历，是一个很常用的方法类。