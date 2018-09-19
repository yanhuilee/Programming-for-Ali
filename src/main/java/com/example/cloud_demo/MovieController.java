package com.example.cloud_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private UserFeignClient userFeignClient;

    @Value("${user.UserServiceUrl}")
    private String userServiceUrl;

    @GetMapping("/user2/{id}")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl + id, User.class);
    }

    @GetMapping("log-instance")
    public void logUserInstance() {
        // 指定微服务主机名
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-provider-user");
        // 打印当前节点
        MovieController.LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(),
                serviceInstance.getPort());
    }

    /**
     * 用 Feign 去调用微服务的API
     * @param id
     * @return
     */
    @GetMapping("/user3/{id}")
    @ResponseBody
    public User findById_feign(@PathVariable Long id) {
        return userFeignClient.findById(id);
    }
}
