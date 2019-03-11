package com.example.mvc.dubbo.provider;

/**
 * @Author: Lee
 * @Date: 2019/02/01 17:57
 */
public class DubboServiceImpl implements DubboService {
    @Override
    public String sayHello(String username) {
        return "hi, " + username;
    }
}
