package com.example.mvc.dubbo.protocol.http;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * @Author: Lee
 * @Date: 2019/02/01 18:09
 */
public class DuHttpServer {

    public void start(String hostName, Integer port) {
        /*Tomcat tomcat = new Tomcat();
        Server tomcatServer = tomcat.getServer();
        Service service = tomcatServer.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(port);

        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostName);

        Host host = new StandardHost();
        host.setName(hostName);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        // 生命周期监听器
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);
        service.setContainer(engine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath, "test", new DuDispatcherServlet());
        context.addServletMappingDecoded("/*", "test");*/

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context context = tomcat.addContext("", "/");
        // 生命周期监听器
        context.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.addServlet("", "test", new DuDispatcherServlet());
        context.addServletMappingDecoded("/*", "test");
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
