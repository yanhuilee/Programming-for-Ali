package com.example.spring;

/**
 * 工厂模式
 * @Author: Lee
 * @Date: 2019/03/13 12:02
 */
public class Factory_ {
    public static void main(String[] args) {
        Fruit fruit = Factory.getInstance("com.example.spring.Apple");
        if (null != fruit) {
            fruit.eat();
        }
    }

}

interface Fruit {
    void eat();
}

class Apple implements Fruit {
    @Override
    public void eat() {
        System.out.println("Apple");
    }
}

class Orange implements Fruit {
    @Override
    public void eat() {
        System.out.println("Orange");
    }
}

class Factory {
    static Fruit getInstance(String className) {
        Fruit fruit = null;
        try {
            fruit = (Fruit) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fruit;
    }
}