package com.example.mvc.dubbo.framework;

import com.example.mvc.dubbo.protocol.http.DuHttpClient;
import com.example.mvc.dubbo.provider.DubboService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: Lee
 * @Date: 2019/02/01 21:01
 */
public class DuProxyFactory<T> {

    public static <T> T getProxy(Class interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                DuHttpClient httpClient = new DuHttpClient();
                DuInvocation invocation = new DuInvocation(DubboService.class.getName(), "sayHello", new Object[]{"124"}, new Class[]{String.class});

                String result = httpClient.post("localhost", 8080, invocation);
                return result;
            }
        });
    }
}
