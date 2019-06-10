package com.example.design_pattern;

/**
 * @Author: Lee
 * @Date: 2019/04/21 02:46
 * 适配器：接口转换
 */
public class Adapter {
    public static void main(String[] args) {
        Duck_ duck_ = new MallardDuck_();
        Turkey turkey = new WildTurkey();
        Duck_ turkeyAdapter = new TurkeyAdapter(turkey);

        System.out.println("the turkey says...");
        turkey.gobble();
        turkey.fly();

        System.out.println("the duck says...");
        duck_.quack();
        duck_.fly();

        System.out.println("the turkeyAdapter says...");
        turkeyAdapter.quack();
        turkeyAdapter.fly();
    }
}

/**
 * 用火鸡冒充鸭子对象
 */
class TurkeyAdapter implements Duck_ {
    Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    @Override
    public void quack() {
        turkey.gobble();
    }

    @Override
    public void fly() {
        for (int i = 0; i < 5; i++) {
            turkey.fly();
        }
    }
}

interface Duck_ {
    void quack();
    void fly();
}

class MallardDuck_ implements Duck_ {

    @Override
    public void quack() {
        System.out.println("quack quack");
    }

    @Override
    public void fly() {
        System.out.println("I'm flying");
    }
}

interface Turkey {
    void gobble();
    void fly();
}

class WildTurkey implements Turkey {

    @Override
    public void gobble() {
        System.out.println("gobble gobble");
    }

    @Override
    public void fly() {
        System.out.println("I'm flying a short distance");
    }
}