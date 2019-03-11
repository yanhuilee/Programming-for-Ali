package com.example.mvc.dubbo.protocol.http;

import com.example.mvc.dubbo.framework.DuInvocation;
import com.example.mvc.dubbo.framework.DuURL;
import com.example.mvc.dubbo.register.DuRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * 处理请求
 * @Author: Lee
 * @Date: 2019/02/01 18:20
 */
public class DuHttpServerHandler {

    public void handler(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = request.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            DuInvocation invocation = (DuInvocation) ois.readObject();

            // 找接口实现类
            String interfaceName = invocation.getInterfaceName();
            DuURL url = new DuURL("localhost", 8080);
            Class implClass = DuRegister.get(url, interfaceName);

            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
            String result = (String) method.invoke(implClass.newInstance(), invocation.getParams());

            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(result, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
