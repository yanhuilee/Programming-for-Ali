package com.example.mvc.config;

import org.springframework.beans.factory.FactoryBean;

/**
 * @Author: Lee
 * @Date: 2019/01/29 18:37
 */
public class PersonFactoryBean implements FactoryBean<Person> {

    @Override
    public Person getObject() throws Exception {
        return new Person("PersonFactoryBean -- getObject()");
    }

    @Override
    public Class<?> getObjectType() {
        return Person.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
