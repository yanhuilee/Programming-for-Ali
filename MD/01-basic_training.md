#### 1、精通java面向对象开发，基础扎实

- 反射机制：动态代理（rpc，aop）
- equals() hashcode()
- foreach循环原理
- [Exception](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/basic/04-Exception.md) : 检查异常(check 显式捕获)，运行时异常（注意NPE）

##### 2、关键字：

```
transient
instanceof
final
static
const
```

- static: 修饰属性、方法和内部类；类加载时存在

类属性中被static所引用的变量，会被作为GC的root根节点。作为根节点就意味着，这一类变量是基本上不会被回收的。因此，static很容易引入内存泄漏的风险

##### 3、IO模型
序列化流

- transient

URLConnection
IOUtils

#### 4、[基础类库](https://github.com/yanhuilee/java_interview/wiki/rt.jar)


- [String](https://github.com/yanhuilee/java_interview/wiki/String)
- Integer、Long、Enum、BigDecimal

##### 自动拆装箱
Integer 缓存机制
