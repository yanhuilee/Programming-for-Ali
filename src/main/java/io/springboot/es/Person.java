package io.springboot.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author lee
 * @Description 用户实体类
 * @Date 20/10/16 17:10
 */
@Data
@AllArgsConstructor
public class Person {

    /**
     * 主键
     */
    @Id
    private Long id;

    private String name;

    /**
     * 国家
     */
    private String country;

    private Integer age;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 介绍
     */
    private String remark;
}
