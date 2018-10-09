package com.example.lambda;

import java.util.Comparator;

public class Apple {

    private Integer weight;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public static void main(String[] args) {
        Comparator c = Comparator.comparing(Apple::getWeight);

    }
}
