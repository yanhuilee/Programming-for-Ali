package com.example.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Apple {

    private Integer weight;

    public Apple(Integer weight) {
        this.weight = weight;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public static void main(String[] args) {
        Comparator c = Comparator.comparing(Apple::getWeight);

        List<Apple> list = new ArrayList<>();
        list.add(new Apple(10));
        list.forEach(apple -> {
            if (apple.getWeight() == 10) {
                apple.setWeight(15);
            }
        });
        list.forEach(apple -> System.out.println("apple = " + apple.getWeight()));
    }
}
