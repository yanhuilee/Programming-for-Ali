<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">问：Java 枚举类比较用 == 还是 equals，有啥区别？</h3>
**答**：没啥区别。因为枚举 Enum 类的 equals 方法默认实现就是通过 == 来比较的；

类似的 Enum 的 compareTo 方法比较的是 Enum 的 ordinal 顺序大小；类似的还有 Enum 的 name() 和 toString() 一样都返回的是 Enum 的 name 值。

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">问：Java 枚举是如何保证线程安全的？</h3>

**答**：因为 Java 类加载与初始化是 JVM 保证线程安全，而 Java enum 枚举在编译器编译后的字节码实质是一个 final 类，每个枚举类型是这个 final 类中的一个静态常量属性，其属性初始化是在该 final 类的 static 块中进行，而 static 的常量属性和代码块都是在类加载时初始化完成的，所以自然就是 JVM 保证了并发安全。

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">问：通过枚举实现一个线程安全的单例？</h3>
//通过枚举实现单例模式
```
public enum Singleton {  
  INSTANCE;  
  public void func() {}  
}
```


```java
enum Operation {
  ADD {
  	@Override
  	public int eval(int a, int b) {return a + b;}
  },
  SUBTRACT {
  	@Override
  	public int eval(int a, int b) {return a - b;}
  },
  MULTIPLY {
  	@Override
  	public int eval(int a, int b) {return a * b;}
  },
  DIVIDE {
  	@Override
  	public int eval(int a, int b) {return a / b;}
  };

  public abstract int eval(int a, int b);
  }
Operation op = Operation.SUBTRACT;
int result = op.eval(2, 3);
```