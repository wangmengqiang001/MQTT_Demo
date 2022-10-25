package com.testmock.springmock.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("test Mockito")
@RunWith(SpringRunner.class)
@SpringBootTest
class IotDeviceCommandTest {
	
	@MockBean
	MqttGateway gateway;
	
	@Autowired
	IotDeviceCommand command;


	@Test
	@DisplayName("测试基本功能")
	@Order(0)
	void testProcess() throws Exception {
		
		
		assertNotNull(gateway);
		assertNotNull(command);
		when(gateway.sendToMqtt("v1/mytopic/device/control", 
				"message for  device{mytopic}"))
		         .thenReturn(true);
		
		
		assertFalse(command.process("abc"));
		
		assertTrue(command.process("mytopic"));
		
	}
	
	@Test
	@Order(10)
	@DisplayName("在设备信息禁止有关键字...")
	void testProcessAir() throws Exception {
		
		
		assertNotNull(gateway);
		assertNotNull(command);
		when(gateway.sendToMqtt("v1/mytopic/device/control", 
				"message for  device{mytopic}"))
		         .thenReturn(true);
		//Mockito.when(gateway.sendToMqtt("topic", "abc")).then();
		try {
			command.process(null);
				fail();
		}catch(Exception e){
			assertEquals("deviceId 不可以为空",e.getMessage());
		}
		
		try {
			command.process("myair");
				fail();
		}catch(Exception e){
			assertEquals("不可以包含字符串air",e.getMessage());
		}
		
		//assertTrue(command.process("mytopic"));
		
	}
	@Test
	@DisplayName("在发送数据时，发生Exception")
	@Order(11)
	void testProcessSendException() {


		assertNotNull(gateway);
		assertNotNull(command);
	

		try {
			when(gateway.sendToMqtt("v1/exception/device/control", 
					"message for  device{exception}"))
			.thenThrow(new Exception("send message exception happened!"));
			assertFalse(command.process("exception"));
			
			when(gateway.sendToMqtt(contains("light"),
					contains("light"))).thenReturn(false);
			assertFalse(command.process("exception"));
				
		}catch (Exception u) {
			fail();
		}
		
	
				


	}
	@Test
	@DisplayName("测试Mvc")	
	@Order(30)
	void testMvc() {
		//TODO 1) test mvc
		
	}
	@Test
	@DisplayName("测试执行顺序")
	@Order(40)
	void testExecuteOrder() {
		//TODO 2) test execute order
		
	}
	@Test
	@DisplayName("测试验证方法")
	@Order(50)
	void testVerify() {
		//TODO 3) test execute order
		
	}
	

}
