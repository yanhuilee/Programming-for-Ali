package com.example.design_pattern;

/**
 * @Author: Lee
 * @Date: 2019/04/15 16:53
 */
public class Strategy {
    public static void main(String[] args) {
        Duck mallard = new MallardDuck();
        mallard.performFly();

        Duck model = new ModelDuck();
        model.performFly();
        model.setFlyBehavior(new FlyRocketPowered());
        model.performFly();
    }
}

abstract class Duck {
    FlyBehavior flyBehavior;

    public Duck() {
    }

    void swim() {
        System.out.println("All ducks float, event decoys!");
    }
    abstract void display();

    void performFly() {
        flyBehavior.fly();
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }
}

/**
 * 绿头鸭
 */
class MallardDuck extends Duck {
    public MallardDuck() {
        flyBehavior = new FlyWithWings();
    }

    void display() {
        System.out.println("I'm a real Mallard Duck");
    }
}

/**
 * 模型鸭
 */
class ModelDuck extends Duck {
    public ModelDuck() {
        flyBehavior = new FlyNoWay();
    }

    void display() {
        System.out.println("I'm a Model Duck");
    }
}

/**
 * 分离分行行为，面向接口编程
 */
interface FlyBehavior {
    void fly();
}

class FlyWithWings implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("FlyWithWings..");
    }
}

class FlyNoWay implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("FlyNoWay..");
    }
}

class FlyRocketPowered implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("FlyRocketPowered..");
    }
}