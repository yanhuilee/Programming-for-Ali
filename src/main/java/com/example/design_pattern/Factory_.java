package com.example.design_pattern;

/**
 * @Author: Lee
 * @Date: 2019/04/20 08:41
 * new xx(factoryType)
 * abstract Product factoryMethod(St type)
 * 简单工厂：创建一个产品
 * 抽象工厂：创建整个产品家族
 */
public class Factory_ {
    public static void main(String[] args) {
        PizzaStore nyStylePizzaStore = new NYStylePizzaStore();
        nyStylePizzaStore.orderPizza("cheese");
    }
}

abstract class PizzaStore {
    Pizza orderPizza(String type) {
        Pizza pizza = createPizza(type);
        // 省略制作过程
        pizza.prepare();
        return pizza;
    }

    /**
     * 由子类决定实例化哪一个类
     */
    abstract Pizza createPizza(String type);
}

class Pizza {
    String name;

    void prepare() {
        System.out.println("您的" + name + "已准备好了");
    }
}

class NYStylePizzaStore extends PizzaStore {

    /**
     * 子类负责创建具体产品
     * @return
     */
    @Override
    Pizza createPizza(String type) {
        // 返回具体类型的产品（Pizza的其一子类）
        Pizza pizza = new Pizza();
        pizza.name = type;
        return pizza;
    }
}