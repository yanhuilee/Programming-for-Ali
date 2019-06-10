>维护人员：**Lee**  
>创建时间：2018-05-02

BeanDefinition：管理基于Spring 应用中的各种对象以及他们之间的相互依赖关系
> IoC容器是用来管理对象依赖关系的，对IoC容器来说，BeanDefinition 就是对依赖反转模式中管理的对象依赖关系的数据抽象，也是容器实现依赖反转功能的核心数据结构，依赖反转功能都是围绕对这个 BeanDefinition 的处理来完成的。

### IoC容器初始化过程
1. Resource 定位
2. BeanDefinition 的载入
3. 向 IoC容器注册 BeanDefinition

- refresh() 启动

BeanDefinition 的 Resource 定位、载入和解析、在IoC容器中注册三个基本过程

#### 1. Resource 定位
> BeanDefinition 资源定位，由 ResourceLoader 通过统一的 Resource 接口来完成

```
public interface ResourceLoader {
    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);
    ClassLoader getClassLoader();
}
```

注：ApplicationContext的所有实现类都实现RecourceLoader接口，因此可以直接调用getResource（参数）获取Resoure对象

#### 2. BeanDefinition 的载入
> 把定义好的 Bean 表示成IoC容器内部的数据结构（BeanDefinition）

```java
protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) {
    // 获取所有resource资源位置
    Resource[] configResources = this.getConfigResources();
    if(configResources != null) {
        // 载入 BeanDefinition
        reader.loadBeanDefinitions(configResources);
    }

    String[] configLocations = this.getConfigLocations();
    if(configLocations != null) {
        reader.loadBeanDefinitions(configLocations);
    }

}
```

#### 3. 向 IoC容器注册 BeanDefinition
> 不同于依赖注入（发生在应用第一次通过getBean()向容器索取Bean的时候）

`DefaultListableBeanDefiniton.registerBeanDefiniton()`利用解析好的BeanDefinition对象完成最终的注册。

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
```
private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);
```
