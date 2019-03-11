package com.example.mvc.dubbo.consumer;

import com.example.mvc.dubbo.framework.DuInvocation;
import com.example.mvc.dubbo.framework.DuProxyFactory;
import com.example.mvc.dubbo.protocol.http.DuHttpClient;
import com.example.mvc.dubbo.provider.DubboService;

/**
 * @Author: Lee
 * @Date: 2019/02/01 20:28
 */
public class DuConsumer {

    public static void main(String[] args) {
        DuHttpClient httpClient = new DuHttpClient();
        DuInvocation invocation = new DuInvocation(DubboService.class.getName(), "sayHello", new Object[]{"124"}, new Class[]{String.class});

        String result = httpClient.post("localhost", 8080, invocation);
        System.out.println("result = " + result);

        DubboService service = DuProxyFactory.getProxy(DubboService.class);
        result = service.sayHello("你妹的");
        System.out.println("result = " + result);
    }
}
