package com.testmock.springmock;

import org.redishash.annotation.EnableRedisHashCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableRedisHashCache
@SpringBootApplication
@ComponentScan({"com.huawen.mqtt.*",
	"com.testmock.*"})
public class SpringmockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringmockApplication.class, args);
	}
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }
    @Bean
    @ConditionalOnBean(annotation=EnableRedisHashCache.class)
    public Object getObject() {
    	System.out.println("EnableRedisHashCache is detected");
       return new Object();	
    }
    

}
