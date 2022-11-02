package com.huawen.mqtt.controller;

import java.util.Random;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huawen.mqtt.bean.MyMessage;
import com.huawen.mqtt.inter.MqttGateway;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

/**
 * @author:xjl
 * @date:2022/5/6 9:17
 * @Description: mqtt发布消息controller
 **/
@Slf4j
@RestController
public class MqttPublishController /*implements MqttPublisherRest*/ {
	@Resource
	private MqttGateway mqttGateWay;

	@Resource
	@Qualifier("inClientFactory")
	private MqttPahoClientFactory mqttClientFactory;

	/* (non-Javadoc)
	 * @see com.huawen.mqtt.controller.MqttPublisherRest#send(com.huawen.mqtt.bean.MyMessage)
	 */
	//@Override
	@PostMapping("/send")
	public String send(@RequestBody MyMessage myMessage) {
		// 发送消息到指定主题
		mqttGateWay.sendToMqtt(myMessage.getTopic(), 1, myMessage.getContent());
		return "send topic: " + myMessage.getTopic() + ", message : " + myMessage.getContent();
	}
	/* (non-Javadoc)
	 * @see com.huawen.mqtt.controller.MqttPublisherRest#send(java.lang.String, java.lang.String)
	 */
	//@Override
	@GetMapping("/send")
	public String send(@RequestParam String topic, 
			@RequestParam String message) {

		//start Thread to wait message
		String url="tcp://guizhou:1883";
		String clientId="xxx_xx123"+RandomString.make(10);
		
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

			long start = System.currentTimeMillis();
			//wait notification
			synchronized(signalLock) {
				signalLock.wait(10000);
			}
			long wait = System.currentTimeMillis() - start;
			log.info("total wait {} ms",wait);
			//get the response then close 
			clientInstance.unsubscribe(replyTopic);
			clientInstance.disconnect();
			if(wait < 10000)
				return "send topic: " + topic + ", message : " + message;
			else {
				return "CANNOT receive message within time";
			}

		} catch (MqttSecurityException e) {
			
			e.printStackTrace();
			return e.getMessage();
		} catch (MqttException e) {
			
			e.printStackTrace();
			return e.getMessage();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			return e.getMessage();
		} catch (Exception e) {
			
			e.printStackTrace();
			return e.getMessage();
		}

		//return "error happens";

		
	}


}
