package com.example.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 组件注册-@Configuration&@Bean给容器中注册组件
 * @Author: Lee
 * @Date: 2019/01/29 17:44
 */
@Configuration
public class MainConfig {

    @Bean("person")
    public Person person() {
        return new Person("xiao");
    }

    /**
     * 返回一个 Person 类型
     * @return
     */
    @Bean
    public PersonFactoryBean personFactoryBean() {
        return new PersonFactoryBean();
    }
}
