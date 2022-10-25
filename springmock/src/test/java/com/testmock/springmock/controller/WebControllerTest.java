package com.testmock.springmock.controller;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc // 自动配置MockMvc
class WebControllerTest {
	
	@Autowired
	MockMvc mvc;
	

	@Test
	void testHome() throws Exception {
		String url = "/home";
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get(url));
		int length = action.andReturn().getResponse()
		.getContentLength();
		
		assertTrue(length > 0);
		
		assertEquals(200,action.andReturn().getResponse().getStatus());
		
		assertEquals("Hello, this is homepage",
				action.andReturn().getResponse().getContentAsString());
	}
	
}
