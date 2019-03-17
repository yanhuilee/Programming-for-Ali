### 容器相关
组件注册
```
@Configuration @Bean("name")  //配置类
@ComponentScan //包扫描
@Scope
@Lazy

@Import(xx.class) //快速导入组件
```

FactoryBean<xx> 注册组件
```
xx getObject() {
	return new xx()
}

Class<?> getObjectType() {
	return xx.class
}

boolean isSingleton() {
	return false
}
```