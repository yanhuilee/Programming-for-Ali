### 建设者模式 Builder
> 简化复杂对象的构造，隐藏了对象构造的复杂性，内部静态类接受链接方法的调用。

org.springframework.beans.factory.support.BeanDefinitionBuilder

这是一个允许我们以编程方式定义bean的类。在关于bean工厂后处理器的文章中会看到它，BeanDefinitionBuilder包含几个方法，它们为AbstractBeanDefinition抽象类的相关实现设置值，比如作用域，工厂方法，属性等。想看看它是如何工作的，请查看以下这些方法：

```java
private final AbstractBeanDefinition beanDefinition;

/**
 * Set the name of the parent definition of this bean definition.
 */
public BeanDefinitionBuilder setParentName(String parentName) {
    this.beanDefinition.setParentName(parentName);
    return this;
}

/**
 * Set the name of the factory method to use for this definition.
 */
public BeanDefinitionBuilder setFactoryMethod(String factoryMethod) {
    this.beanDefinition.setFactoryMethodName(factoryMethod);
    return this;
}

...
```

### 抽象工厂 BeanFactory
```
getBean() //返回已创建的对象（共享实例，单例作用域）或初始化新的对象（原型作用域）
```

### 代理模式
> 代理对象不仅可以覆盖真实对象，还可以扩展其功能

org.springframework.aop.framework.ProxyFactoryBean

该工厂根据Spring bean构建AOP代理。该类实现了定义getObject()方法的FactoryBean接口。此方法用于将需求Bean的实例返回给bean factory。在这种情况下，它不是返回的实例，而是AOP代理。在执行代理对象的方法之前，可以通过调用补充方法来进一步“修饰”代理对象

ProxyFactory的一个例子是：

```java
public class TestProxyAop {

	@Test
	public void test() {
		ProxyFactory factory = new ProxyFactory(new House());
		factory.addInterface(Construction.class);
		factory.addAdvice(new BeforeConstructAdvice());
		factory.setExposeProxy(true);

		Construction construction = (Construction) factory.getProxy();
		construction.construct();
		assertTrue("Construction is illegal. "
		  + "Supervisor didn't give a permission to build "
		  + "the house", construction.isPermitted());
	}

}

interface Construction {
	public void construct();
	public void givePermission();
	public boolean isPermitted();
}

class House implements Construction{

	private boolean permitted = false;

	@Override
	public boolean isPermitted() {
		return this.permitted;
	}

	@Override
	public void construct() {
		System.out.println("I'm constructing a house");
	}

	@Override
	public void givePermission() {
		System.out.println("Permission is given to construct a simple house");
	this.permitted = true;
	}
}

class BeforeConstructAdvice implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] arguments, Object target) throws Throwable {
		if (method.getName().equals("construct"))
			((Construction) target).givePermission();
	}

}
```

这个测试应该通过，因为我们不直接在House实例上操作，而是代理它。代理调用第一个BeforeConstructAdvice的before方法（指向在执行目标方法之前执行，在我们的例子中为construct()）通过它，给出了一个“权限”来构造对象的字段（house）。代理层提供了一个额外新功能，因为它可以简单地分配给另一个对象。要做到这一点，我们只能在before() 之前修改过滤器。



，复合，策略，模板
RestTemplate

