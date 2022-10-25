package com.testmock.springmock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@SpringBootApplication
@ComponentScan({"com.huawen.mqtt.*"})
public class SpringmockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringmockApplication.class, args);
	}
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

}
