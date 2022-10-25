package com.testmock.springmock.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationInitializer implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.warn("service is starting,  begin initialization....");
		System.out.println("this is start message!");

	}

}
