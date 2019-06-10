`getBean(...)`，只是 bean 实例化进程的入口，真正的实现逻辑其实是在 AbstractAutowireCapableBeanFactory 的 #doCreateBean(...) 中实现，实例化过程如下图：

![bean实例化过程](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/images/bean实例化过程.png)

Spring 容器将会对其所有管理的 Bean 对象全部给予一个统一的生命周期管理，同时在这个阶段我们也可以对其进行干涉（比如对 bean 进行增强处理，对 bean 进行篡改），如上图。



![bean从开始初始化到销毁会经历的方法调用](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/images/bean从开始初始化到销毁会经历的方法调用.jpg)