package com.example.mvc.test;

import com.example.mvc.annotation.MyController;
import com.example.mvc.annotation.MyRequestMapping;
import com.example.mvc.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Lee
 * @Date: 2019/01/27 03:57
 */
@MyController
@MyRequestMapping("/mvc")
public class TestController {

    /**
     * http://localhost/mvc/doTest1?param=li
     * @param request
     * @param response
     * @param param
     */
    @MyRequestMapping("/doTest1")
    public void test1(HttpServletRequest request, HttpServletResponse response,
                      @MyRequestParam("param") String param) {
        System.out.println("param: " + param);
        try {
            response.getWriter().write("doTest method success! param:" + param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
