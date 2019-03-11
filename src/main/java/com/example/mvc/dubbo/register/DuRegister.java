package com.example.mvc.dubbo.register;

import com.example.mvc.dubbo.framework.DuURL;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册中心
 * @Author: Lee
 * @Date: 2019/02/01 18:01
 */
public class DuRegister {

    public static Map<String, Map<DuURL, Class>> REGISTER = new HashMap<>();

    /**
     * 注册服务
     * @param url
     * @param interfaceName
     * @param implClass
     */
    public static void regist(DuURL url, String interfaceName, Class implClass) {
        Map<DuURL, Class> map = new HashMap<>();
        map.put(url, implClass);
        REGISTER.put(interfaceName, map);
    }

    public static DuURL get(String interfaceName) {
        return null;
    }

    public static Class get(DuURL url, String interfaceName) {
        return REGISTER.get(interfaceName).get(url);
    }
}
