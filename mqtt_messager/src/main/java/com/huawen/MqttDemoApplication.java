package com.huawen;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author xujl
 * @date 2022/5/5 17:05
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableDubbo
public class MqttDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttDemoApplication.class, args);
    }
    
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

}
