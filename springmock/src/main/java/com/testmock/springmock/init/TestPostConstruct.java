package com.testmock.springmock.init;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.testmock.springmock.controller.WebController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TestPostConstruct {
	
	@Autowired
	private WebController web;
	
	@PostConstruct
	public void init() {
		log.info("post construct is invoked,here!!!");
		
		
	}

}
