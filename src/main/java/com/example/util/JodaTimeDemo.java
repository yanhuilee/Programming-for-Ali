package com.example.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 替代 DateUtils
 * @Author: Lee
 * @Date: 2019/01/08 13:38
 */
public class JodaTimeDemo {

    public static void main(String[] args) {
        // 初始化时间，2017.06.21 18:00:00
        DateTime dateTime = new DateTime(2017, 6, 21, 18, 00, 0);
        System.out.println("dateTime = " + dateTime.toString("yyyy-MM-dd"));
        // 解析时间字符串
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dateTime1 = DateTime.parse("2017-06-21", formatter);

        // 时间计算
        dateTime.plusDays(1); //增加天
        DateTime.Property month = dateTime.monthOfYear();
        System.out.println("是否闰月 = " + month.isLeap());
    }
}
