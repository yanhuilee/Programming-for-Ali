#### Bean：
Bean 的生命周期由 IoC 容器控制。IoC 容器定义 Bean 操作的规则，即 Bean 的定义（BeanDefinition）


#### IoC 之深入分析 Bean 的生命周期
1. bean 实例化 doCreateBean()
2. 激活 Aware
3. BeanPostProcessor: 在 Bean 实例化之后初始化之际对 Bean 进行增强处理
4. InitializingBean 和 init-method
5. DisposableBean 和 destroy-method

Spring 容器将会对其所有管理的 Bean 对象全部给予一个统一的生命周期管理，同时在这个阶段我们也可以对其进行干涉（比如对 bean 进行增强处理，对 bean 进行篡改），如上图。



![bean从开始初始化到销毁会经历的方法调用](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/images/bean从开始初始化到销毁会经历的方法调用.jpg)