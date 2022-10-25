package com.testmock.springmock;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author wmqiang
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("测试上下文加载")
public class SpringmockApplicationTests {


	@Test
	@DisplayName("2. 正常注入")	
	@Order(10)	
	public void contextLoads() {
	}
	
	@Test
	@Order(5)
	@DisplayName("1. 注入失败")
	public void acontextLoads() {
	}

}
