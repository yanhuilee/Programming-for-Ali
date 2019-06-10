### 深入分析 InitializingBean 和 init-method

```java
public interface InitializingBean {
    /**
     * 该方法在 BeanFactory 设置完了所有属性之后被调用
     * 该方法允许 bean 实例设置了所有 bean 属性时执行初始化工作
     */
    void afterPropertiesSet() throws Exception;
}
```

Spring 在完成实例化后，设置完所有属性，进行 "Aware 接口" 和 "BeanPostProcessor 前置处理"之后，会接着检测当前 bean 对象是否实现了 InitializingBean 接口。如果是，则会调用其 #afterPropertiesSet()，进一步调整 bean 实例对象的状态。


#### invokeInitMethods
bean 初始化阶段（ `initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd)`）， Spring 容器会主动检查当前 bean 是否已经实现了 `InitializingBean` 接口。

这个主动检查、调用的动作是由 `invokeInitMethods(String beanName, final Object bean, @Nullable RootBeanDefinition mbd)` 

然后，再检查是否也指定了 `init-method`，如果指定了则通过反射机制调用