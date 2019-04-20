package com.example.design_pattern;

/**
 * 装饰者
 * @Author: Lee
 * @Date: 2019/03/13 02:27
 * BufferedInputStream(InputStream in) {super(in); --> protected volatile InputStream in;}
 */
public class Decorator_ {
    public static void main(String[] args) {
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription() + ", $" + beverage.cost());

        beverage = new Milk(beverage);
        System.out.println(beverage.getDescription() + ", $" + beverage.cost());
    }

}

abstract class Beverage {
    protected String description = "Unknown Beverage";

    public String getDescription() {
        return description;
    }

    public abstract double cost();
}

class HouseBlend extends Beverage {

    public HouseBlend() {
        description = "HouseBlend";
    }

    @Override
    public double cost() {
        return 0.89;
    }
}

class DarkRoast extends Beverage {
    public DarkRoast() {
        description = "DarkRoast";
    }

    @Override
    public double cost() {
        return 1.05;
    }
}

class Espresso extends Beverage {
    public Espresso() {
        description = "Espresso";
    }

    @Override
    public double cost() {
        return 2;
    }
}

class Decat extends Beverage {
    public Decat() {
        description = "Decat";
    }

    @Override
    public double cost() {
        return 0.99;
    }
}

abstract class CondimentDecorator extends Beverage {
    public abstract String getDescription();
}

class Milk extends CondimentDecorator {
    Beverage beverage;

    public Milk(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public double cost() {
        return beverage.cost() + 0.3;
    }

    @Override
    public String getDescription() {
        return beverage.description + " , Milk";
    }
}