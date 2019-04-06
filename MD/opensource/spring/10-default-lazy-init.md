spring中的default-lazy-init参数和lazy-init
在spring的配置中的根节点上有个  default-lazy-init="true"配置：

1、spring的default-lazy-init参数
此参数表示延时加载，即在项目启动时不会实例化注解的bean，除非启动项目时需要用到，未实例化的注解对象在程序实际访问调用时才注入调用

spring在启动的时候，default-lazy-init参数默认为false，会默认加载整个对象实例图，从初始化ACTION配置、到 service配置到dao配置、乃至到数据库连接、事务等等。这样可以减少web服务器在运行时的负担，但是对于开发者来说无疑是效率极低的一个设置了。

spring提供了default-lazy-init属性，其配置形式如下，applicationContext.xml中：

```
<beans  default-lazy-init ="true" >
　　.......  
</beans>
```

2、Spring 中lazy-init 和abstract 属性
1.lazy-init

```
<beans>
    <bean id="service1" type="bean路径" lazy-init="true"/>
    <bean id="service2" type="bean路径" lazy-init="false">
        <property name="service1" ref="service1"/>
    </bean>
</beans>
```

以上两个bean,一个lazy-init属性为true,一个为false，由什么区别呢？

当 IoC容器启动时，service2会实例化，而service1则不会；但是当容器实例化service2时，service1也被实例化了，为什么呢，因为service2需要它。也就是说lazy-init="true"的bean，IoC容器启动时不会实例化该bean，只有当容器需要用到时才实例化它。lazy-init有利于容器效率，对于不需要的bean可以先不管。

同时我们可以针对具体的模块在相应的bean里面使用lazy-init 属性，lazy-init 比default-lazy-init的==优先级高==。
spring注解可使用@Lazy(false)注解标签注解在类名上

2.abstract  

```
<bean id="baseTxService"  class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean"  abstract="true">
</bean>
```
bean abstract="true"时，该bean不会被实例化

当然，也不是所有的beans都能设置default-lazy-init成为true.对于scheduler的bean不能用lazy-init
