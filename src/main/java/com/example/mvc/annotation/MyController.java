package com.example.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建自己的Controller注解，只能标注在类上面
 * @Author: Lee
 * @Date: 2019/01/27 03:17
 */
@Target(ElementType.TYPE)
// 注解生命周期，不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {

    String value() default "";
}
