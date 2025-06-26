package org.csu.userserviceconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        return this.restTemplate.getForObject("http://localhost:8080/hello", String.class);
    }
}
