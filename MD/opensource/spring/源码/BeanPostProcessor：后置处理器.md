> BeanPostProcessor 的作用：在 Bean 完成实例化后，如果我们需要对其进行一些配置、增加一些自己的处理逻辑

BeanPostProcessor 可以理解为是 Spring 的一个工厂钩子（其实 Spring 提供一系列的钩子，如 Aware 、InitializingBean、DisposableBean），它是 Spring 提供的对象实例化阶段强有力的扩展点，允许 Spring 在实例化 bean 阶段对其进行定制化修改，比较常见的使用场景是处理标记接口实现类或者为当前对象提供代理实现（例如 AOP）。

一般普通的 BeanFactory 是不支持自动注册 BeanPostProcessor 的，需要我们手动调用 `addBeanPostProcessor(BeanPostProcessor beanPostProcessor)` 进行注册。注册后的 BeanPostProcessor 适用于所有该 BeanFactory 创建的 bean，但是 ApplicationContext 可以在其 bean 定义中自动检测所有的 BeanPostProcessor 并自动完成注册，同时将他们应用到随后创建的任何 Bean 中。

前后置处理器，他们应用 `invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)` 的前后。如下图：
![BeanPostProcessor后置处理器流程](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/images/BeanPostProcessor后置处理器流程.png)

```java
Object wrappedBean = bean;
if (mbd == null || ! mbd.isSyncthetic()) {
	wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
}

try {
	invokeInitMethods(beanName, wrappedBean, mbd);
} catch (Throwable ex) {

}
if (mbd == null || !mbd.isSyncthetic()) {
	wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
}

return wrappedBean;
```

```java
public class BeanPostProcessorTest implements BeanPostProcessor {

    /**
     * bean：已经实例化了的 instanceBean
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Bean [" + beanName + "] 开始初始化");
        // 这里一定要返回 bean
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Bean [" + beanName + "] 完成初始化");
        return bean;
    }

    public void display(){
        System.out.println("hello BeanPostProcessor!!!");
    }
}
```