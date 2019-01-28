package com.example.mvc.servlet;

import com.example.mvc.annotation.MyAutowired;
import com.example.mvc.annotation.MyController;
import com.example.mvc.annotation.MyRequestMapping;
import com.example.mvc.annotation.MyService;
import com.google.common.base.CaseFormat;
import org.springframework.util.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: Lee
 * @Date: 2019/01/27 03:35
 */
@WebServlet(urlPatterns = "/mvc/*")
public class MyDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();
    private List<String> classNames = new ArrayList<>();
    private Map<String, Object> ioc = new HashMap<>();
    private Map<String, Method> handlerMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1.加载配置文件 application.properties
//        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        // 2.初始化所有相关联的类，扫描用户设定包下类
        doScanner("com.example.mvc.test");
        // 3.实例化,并且放到ioc容器中(beanName : bean)
        doInstance();

        // 依赖注入
        doAutowired();
        // 4.初始化HandlerMapping(将url和method对应)
        initHandlerMapping();
        System.out.println("MyDispatcherServlet 已经初始化完成。。。。。。");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * 处理请求
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handlerMapping.isEmpty()) {
            return;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 Not Found!!");
            return;
        }

        Method method = this.handlerMapping.get(url);
        // 获取方法参数列表
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 请求参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        // 保存参数值
        Object[] paramValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class) {
                paramValues[i] = req;
                continue;
            }
            if (parameterType == HttpServletResponse.class) {
                paramValues[i] = resp;
                continue;
            }
            if (parameterType == String.class) {
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("[|]", "").replaceAll(",\\s", ",");
                    paramValues[i] = value;
                }
            }
        }

        // 反射调用
        String baseName = lowerFirstCase(method.getDeclaringClass().getSimpleName());
        method.invoke(this.ioc.get(baseName), paramValues);
    }

    /**
     * 加载 web.xml
     *
     * @param location
     */
    private void doLoadConfig(String location) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(location);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 包扫描，递归扫描出所有的Class文件
     * @param packageName
     */
    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                // 递归读取
                doScanner(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                classNames.add(className.trim());
            }
        }
    }

    /**
     * 初始化所有相关的类，并放入到IOC容器
     */
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyController.class)) {
                    // 首字母小写
                    ioc.put(lowerFirstCase(clazz.getSimpleName()), clazz.newInstance());
                } else if (clazz.isAnnotationPresent(MyService.class)) {

                    MyService myService = clazz.getAnnotation(MyService.class);
                    String baseName = myService.value();
                    if (!StringUtils.isEmpty(baseName)) {
                        ioc.put(baseName, clazz.newInstance());
                    } else {

                        Class<?>[] interfaces = clazz.getInterfaces();
                        for (Class<?> aClass : interfaces) {
                            ioc.put(aClass.getName(), clazz.newInstance());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        ioc.entrySet().forEach(entry -> {
            // 获取实例对象属性
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(MyAutowired.class)) {
                    continue;
                }
                MyAutowired autowired = field.getAnnotation(MyAutowired.class);
                String baseName = autowired.value().trim();
                if ("".equals(baseName)) {
                    baseName = field.getType().getName();
                }
                // 设置私有属性访问权限
                field.setAccessible(true);

                try {
                    field.set(entry.getValue(), ioc.get(baseName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化HandlerMapping(将url 和 method对应)
     */
    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<? extends Object> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }

            // 拼接完整 url
            String baseUrl = "";
            if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
                baseUrl = annotation.value();
            }
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }
                MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
                String url = (baseUrl + "/" + annotation.value()).replaceAll("/+", "/");
                handlerMapping.put(url, method);

                System.out.println(url + "-----------------------------" + method);
            }
        }
    }



    /**
     * 首字母小写
     * @param source
     * @return
     */
    private String lowerFirstCase(String source) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, source);
    }
}
