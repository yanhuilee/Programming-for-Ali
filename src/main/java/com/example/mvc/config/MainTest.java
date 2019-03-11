package com.example.mvc.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Lee
 * @Date: 2019/01/29 17:48
 */
public class MainTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        Person person = applicationContext.getBean(Person.class);
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : beanNamesForType) {
            System.out.println("name = " + name);
        }

        /**
         * beanDefinitionName = org.springframework.context.annotation.internalConfigurationAnnotationProcessor
         * beanDefinitionName = org.springframework.context.annotation.internalAutowiredAnnotationProcessor
         * beanDefinitionName = org.springframework.context.annotation.internalRequiredAnnotationProcessor
         * beanDefinitionName = org.springframework.context.annotation.internalCommonAnnotationProcessor
         * beanDefinitionName = org.springframework.context.annotation.internalPersistenceAnnotationProcessor
         * beanDefinitionName = org.springframework.context.event.internalEventListenerProcessor
         * beanDefinitionName = org.springframework.context.event.internalEventListenerFactory
         * beanDefinitionName = mainConfig
         * beanDefinitionName = person
         */
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println("beanDefinitionName = " + beanDefinitionName);
        }
    }
}
