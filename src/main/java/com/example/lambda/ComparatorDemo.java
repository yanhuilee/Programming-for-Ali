package com.example.lambda;

import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;

/**
 * Comparator和排序
 * Comparator.comparing(Human::getName)
 * .thenComparing(Human::getAge))
 * @Author: Lee
 * @Date: 9/24/2018 9:50 PM
 */
public class ComparatorDemo {
    public static void main(String[] args) {
        // 之前，Collections.sort(humans, comparator)
        Comparator<Human> comparator = new Comparator<Human>() {

            @Override
            public int compare(Human o1, Human o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };

        List<Human> humans = Lists.newArrayList(new Human("Sarah", 10), new Human("Jack", 12));
        humans.sort((Human h1, Human h2) -> h1.getName().compareTo(h2.getName()));
//        Comparator.comparing(Human::getName);
//        Assert.assertEquals(humans.get(0).getName(), "Jack");
        System.out.println(humans.get(0).getName().equals("Jack"));

    }
}

class Human {
    private String name;
    private int age;

    public Human(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
