### api-gateway 网关
```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
```
application.yml
```sh
spring:
  application:
    name: api-gateway
  cloud:
    config:
      discovery:
        enabled: true
        service-id: CONFIG
      profile: dev
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
zuul:
  routes:
    aaaaaa:
      path: /myProduct/**
      serviceId: product
      sensitiveHeaders:

  # 排除某些路由
  ignored-patterns:
    - /**/product/listForOrder
management:
  security:
    enabled: false
```

```java
@EnableZuulProxy

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
@Component
public class TokenFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 这里从url参数里获取, 也可以从cookie, header里获取
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
```

---
### client
```
spring-cloud-starter-netflix-eureka-client
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/,http://localhost:8763/eureka/
spring:
  application:
    name: client

@EnableDiscoveryClient
```

---
### config 连接配置中心
```
spring-cloud-config-server
spring:
  application:
    name: config
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/SpringCloud_Sell/config-repo
          username: lly835@163.com
          password: T#27h*E$cg@%}j
          basedir: /Users/admin/code/myProjects/java/imooc/SpringCloud_Sell/config/basedir
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
management:
  endpoints:
    web:
      expose: "*"

@EnableConfigServer
```

### config-repo 配置中心
order-test.yml
```
spring:
  application:
    name: order
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: 123
    url: jdbc:mysql://127.0.0.1:3306/SpringCloud_Sell?characterEncoding=utf-8&useSSL=false
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
env:
  dev
```

---
### eureka-server
```
spring-cloud-starter-netflix-eureka-server

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: false
  server:
    enable-self-preservation: false
spring:
  application:
    name: eureka
server:
  port: 8761

@EnableEurekaServer
```

### order
```
spring-cloud-starter-netflix-eureka-client
spring-boot-starter-data-jpa
mysql-connector-java
spring-cloud-starter-openfeign
spring-cloud-config-client

spring:
  application:
    name: order
  cloud:
    config:
      discovery:
        enabled: true
        service-id: CONFIG
      profile: test
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.imooc.product.client")
public class OrderApplication {}
```
- 商品表
- 订单表
- 订单明细
```
@Entity
public class OrderDetail {
    @Id
    private String detailId;
    /** 订单id. */
    private String orderId;
    /** 商品id. */
    private String productId;
    /** 商品名称. */
    private String productName;
    /** 商品单价. */
    private BigDecimal productPrice;
    /** 商品数量. */
    private Integer productQuantity;
    /** 商品小图. */
    private String productIcon;
}
```
