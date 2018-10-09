package com.example.lambda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void process(Runnable r) {
        r.run();
    }

    public static String processFile() throws IOException {
        try (BufferedReader br = new BufferedReader((new FileReader("")))) {
            return br.readLine();
        }
    }

    public static void main(String[] args) {
        process(() -> System.out.println("This is awesome!"));
        new Thread(() -> System.out.println("This is awesome!")).start();

        // 迭代
        List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        features.forEach(n -> System.out.println("n = " + n));
        features.forEach(System.out::println);

        // Map，为每个订单都加上12%的税
        List<Double> costBeforeTax = Arrays.asList(100.0, 200.0, 300.0, 400.0, 500.0);
        costBeforeTax.stream().map(cost -> cost + cost * 0.12).forEach(System.out::println);
        // 计算总账
        double bill = costBeforeTax.stream().map(cost -> cost + cost * 0.12).reduce((sum, cost) -> sum + cost).get();
        System.out.println("bill = " + bill);

        // 创建一个字符串列表，每个字符串长度大于2
        List<String> filtered = features.stream().filter(str -> str.length() > 10).collect(Collectors.toList());
        System.out.printf("Original List : %s, filtered list : %s %n", features, filtered);
    }

}
