package com.testmock.springmock.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.testmock.springmock.bean.MyMessage;

import lombok.extern.slf4j.Slf4j;
@Slf4j


//@EnableFeignClients
@SpringBootTest(classes=com.testmock.springmock.SpringmockApplication.class)
@SpringBootApplication
class MqttPublisherRestTest {
	
	@Autowired
	private MqttPublisherRest publisher;
	


	@Test
	void testSendMyMessage() {
		assertNotNull(publisher);
		
		MyMessage myMessage = new MyMessage();
		myMessage.setTopic("test topic");
		myMessage.setContent("message for test topic");
		
		String value = publisher.send(myMessage );
		
		assertEquals("send topic: " + myMessage.getTopic() + ", message : " + myMessage.getContent(),
				value);
		
	}

	@Test
	void testSendStringString() {
		assertNotNull(publisher);
	
	}
	@Test
	void testSendOnce() throws Exception {
		String topic="v2/action/command";
		Random random = new Random();
		Integer respId = random.nextInt();
		String message="go on " + respId;
		
		
		
		String value = publisher.send(topic,message );

				
				assertEquals("send topic: " + topic + ", message : " + message,
						value);
	}
	@Test
	void testSendMultiThread() throws Exception {
		final int TOTAL = 2000;
		final int THREADPOOLSIZE = 150;
		ExecutorService exesvc = Executors.newFixedThreadPool(THREADPOOLSIZE);
		String topic="v2/action/command";
		Random random = new Random();
	
	
		
		AtomicInteger success = new AtomicInteger();
		AtomicInteger fail = new AtomicInteger();
		LinkedBlockingQueue<String> responseMsg = new LinkedBlockingQueue();
		
		
		for(int n=0; n<TOTAL; n++)
			exesvc.submit(() ->
			{
				try {

					Integer respId = random.nextInt();
					String message="go on " + respId;					

					
					String response = publisher.send(topic,message );
					
					
					if(	!("send topic: " + topic + ", message : " + message).equals(response)){
						fail.incrementAndGet();
						responseMsg.add(response);
					}else {
						success.incrementAndGet();
					}
					
					//response.getContentAsString();
					
				} catch (Exception e) {
					fail.incrementAndGet();
					// TODO Auto-generated catch block
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
