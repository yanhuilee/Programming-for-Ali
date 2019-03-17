#### 建设者模式 Builder
> 简化复杂对象的构造

BeanDefinitionBuilder
```
private final AbstractBeanDefinition beanDefinition;
public BeanDefinitionBuilder setParentName(String parentName) {
    this.beanDefinition.setParentName(parentName);
    return this;
}

public BeanDefinitionBuilder setFactoryMethod(String factoryMethod) {
    this.beanDefinition.setFactoryMethodName(factoryMethod);
    return this;
}
```

#### 抽象工厂 BeanFactory
getBeana() //返回已创建的对象

RestTemplate