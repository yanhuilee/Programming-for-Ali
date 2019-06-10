package Eatuul.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 网关入口，逻辑也十分简单，分为三步
 * (1)将request,response放入threadlocal中
 * (2)执行三组过滤器
 * (3)清除threadlocal中的的环境变量
 * @Author: Lee
 * @Date: 2019/05/06 03:11
 */
public class EatuulServlet extends HttpServlet {
    private EatRunner eatRunner = new EatRunner();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 将request，response放入上下文对象
        eatRunner.init(req, resp);
        try {
            // 执行前置过滤
            eatRunner.preRoute();
            eatRunner.route();
            // 执行后置过滤
            eatRunner.postRoute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
