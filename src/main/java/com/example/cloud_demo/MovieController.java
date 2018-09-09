package com.example.cloud_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("user.UserServiceUrl")
    private String userServiceUrl;

    @GetMapping("/user2/{id}")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl + id, User.class);
    }
}
