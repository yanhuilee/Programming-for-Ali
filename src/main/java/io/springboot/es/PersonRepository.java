package io.springboot.es;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lee
 * @Description TODO
 * @Date 20/10/16 23:16
 */
@Repository
public interface PersonRepository {

    /**
     * 根据年龄区间查询
     *
     * @param min 最小值
     * @param max 最大值
     * @return 满足条件的用户列表
     */
    List<Person> findByAgeBetween(Integer min, Integer max);
}
