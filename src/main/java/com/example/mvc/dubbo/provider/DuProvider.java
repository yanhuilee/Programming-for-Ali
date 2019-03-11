package com.example.mvc.dubbo.provider;

import com.example.mvc.dubbo.framework.DuURL;
import com.example.mvc.dubbo.protocol.http.DuHttpServer;
import com.example.mvc.dubbo.register.DuRegister;

/**
 * @Author: Lee
 * @Date: 2019/02/01 18:33
 */
public class DuProvider {

    public static void main(String[] args) {
        // 注册服务
        DuURL url = new DuURL("localhost", 8080);
        DuRegister.regist(url, DubboService.class.getName(), DubboServiceImpl.class);

        // 启动Tomcat
        DuHttpServer httpServer = new DuHttpServer();
        httpServer.start(url.getHost(), url.getPort());
    }
}
