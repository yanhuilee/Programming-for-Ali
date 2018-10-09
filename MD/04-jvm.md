- 类加载机制：双亲委派原则，当前类加载器需要先去请求父加载器加载当前类，无法完成才自己去尝试加载

- 运行时内存：程序计数器，堆栈，方法区，堆外内存

- Java内存模型：Java的主内存+线程私有内存模型是线程安全问题产生的根本

- GC原理和调优：参数优化配置，新生代和老年代垃圾回收器选择，垃圾回收参数配置

[ClassLoader机制](https://github.com/yanhuilee/java_interview/wiki/ClassLoader机制)

#### 垃圾回收
内存分配策略、垃圾收集器（G1）、GC参数、对象存活判定
##### 1、GC机制 - 自动内存管理(内存分配原则)
##### 2、何时被回收
可达性分析：GC Roots
- 栈帧中局部变量表中引用的对象
- 方法区中静态属性和常量
- 本地方法栈 Native 引用的...

##### 3、GC算法
- 标记清除
- 复制
- 标记整理
- 分代收集

##### 4、并行收集器
##### 5、内存分配与回收策略
- Eden
- O: 大对象：长字符串，[]; 存活15次

##### 6、内存泄露
OutOfMemoryError StackOverflowError

#### 性能调优和监控工具
jps、jstack、jmap、jstat、jconsole、jinfo、javap、jhat、btrace、TProfiler

##### HotSpot
即时编译器、编译优化
