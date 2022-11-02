package com.huawen.mqtt.controller;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.huawen.mqtt.inter.MqttGateway;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@AutoConfigureMockMvc // 自动配置MockMvc
@SpringBootTest(classes=com.huawen.MqttDemoApplication.class)
class MqttPublishControllerTest {
	

	 
	@Autowired
	MockMvc mvc;
	
	@Resource
	@Qualifier("outClientFactory")
	private MqttPahoClientFactory mqttClientFactory;
	
	@Resource
	private MqttGateway mqttGateWay;

	@Test
	void testSendStringString() {
		assertNotNull(mqttClientFactory);
		System.out.println("clientFactory: "+mqttClientFactory );
		
		String topic="v2/action/command";
		String message="go on ";
		//start Thread to wait message
				String url="tcp://guizhou:1883";
				String clientId="xxx_xx123";

				try {
					IMqttClient clientInstance = mqttClientFactory.getClientInstance(url, clientId);
					Object signalLock = new Object(); 
					Random random = new Random();
					Integer respId = random.nextInt();
					String replyTopic = "v2/"+respId+"/response";
					String resTopic = "<reply>v2/"+respId+"/response</reply>";
					
					
					clientInstance.connect();
					clientInstance.subscribe(replyTopic, new ResponseMessageListener(signalLock));


					mqttGateWay.sendToMqtt(topic, 1, message+resTopic);

					//wait notification
					synchronized(signalLock) {
						signalLock.wait(40000);
					}
					//get the response then close 
					clientInstance.unsubscribe(replyTopic);
					clientInstance.disconnect();

				} catch (MqttSecurityException e) {
					
					e.printStackTrace();
				} catch (MqttException e) {
					
					e.printStackTrace();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
	}
	
	@Test
	void testSendOnce() throws Exception {
		String topic="v2/action/command";
		Random random = new Random();
		Integer respId = random.nextInt();
		String message="go on " + respId;
		
		String url = "/send";
		
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get(url).
				param("topic", topic).param("message",message ));
		int length = action.andReturn().getResponse()
		.getContentLength();
		
		assertTrue(length > 0);
		
		assertEquals(200,action.andReturn().getResponse().getStatus());
		
		assertEquals("send topic: " + topic + ", message : " + message,
				action.andReturn().getResponse().getContentAsString());
	}
	@Test
	void testSendMulti() throws Exception {
		for(int n=0; n<100; n++)
			testSendOnce();
	}
	@Test
	void testSendMultiThread() throws Exception {
		final int TOTAL = 2000;
		final int THREADPOOLSIZE = 150;
		ExecutorService exesvc = Executors.newFixedThreadPool(THREADPOOLSIZE);
		String topic="v2/action/command";
		Random random = new Random();
		String url = "/send";
		String urltmp = url+"?topic={value}&message={value}"; 
		
		AtomicInteger success = new AtomicInteger();
		AtomicInteger fail = new AtomicInteger();
		LinkedBlockingQueue<String> responseMsg = new LinkedBlockingQueue<String>();
		
		
		for(int n=0; n<TOTAL; n++)
			exesvc.submit(() ->
			{
				try {

					Integer respId = random.nextInt();
					String message="go on " + respId;					

					
					//MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
	//			　　　　　　 SecurityUtils.setSecurityManager(wac.getBean(SecurityManager.class);
				 
					// ResultActions action = mvc.perform(MockMvcRequestBuilders.get(url) 
					//param("topic", topic).param("message",message ));
					
					ResultActions action = mvc.perform(MockMvcRequestBuilders.get(urltmp,topic,message));
							
					MockHttpServletResponse response = action.andReturn().getResponse();
					if(response.getStatus()!=200 ||
							!("send topic: " + topic + ", message : " + message).equals(response.getContentAsString())){
						fail.incrementAndGet();
						responseMsg.add(response.getContentAsString());
					}else {
						success.incrementAndGet();
					}
					
					//response.getContentAsString();
					
				} catch (Exception e) {
					fail.incrementAndGet();
				
					e.printStackTrace();
				}
			});
		exesvc.shutdown();
		exesvc.awaitTermination(200, TimeUnit.SECONDS);
		
		log.info("success:{},fail:{}",success,fail);
		assertEquals(TOTAL,fail.get()+success.get());
		
		responseMsg.forEach(e ->
			log.info("error: {}",e));

		assertEquals(TOTAL,success.get());
		assertEquals(0,fail.get());
	}


}
