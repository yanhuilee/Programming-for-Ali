package com.example.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * lambda 使用 map filter collect
 * IntSummaryStatistics
 * @Author: Lee
 * @Date: 9/24/2018 9:22 PM
 */
public class LambdaDemo {

    public static void main(String[] args) {
        // 例7、通过过滤创建一个String列表
        List<String> strList = new ArrayList();
        List<String> filtered = strList.stream().filter(x -> x.length() > 4)
                .collect(Collectors.toList());

        // 将字符串换成大写并用逗号链接起来
        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.println("G7Countries = " + G7Countries);

        // 用所有不同的数字创建一个正方形列表
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().map(x -> x * x).distinct().collect(Collectors.toList());
        // TODO: 9/24/2018 搞不懂啊，为什么要这么输出
        System.out.printf("Square Without duplicates : %s %n", distinct);

        // 获取数字的个数、最小值、最大值、总和以及平均值
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println("stats.getSum() = " + stats.getSum());

    }
}
