package com.testmock.springmock.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class IotDeviceCommand {
	
	@Autowired
	MqttGateway gateway;
	
	
	public boolean process(String deviceId) {
		String topic = getTopic(deviceId);
		String msg = createMessage(deviceId);
	
		boolean sentOK = false;
		try {
			sentOK = gateway.sendToMqtt(topic, msg);
		}catch(Exception e) {
			//ignore
		}
		return sentOK;
		
	}


	private String createMessage(String deviceId) {
		Assert.doesNotContain(deviceId, "air", "不可以包含字符串air");
		return "message for  device{"+deviceId+"}" ;
	}


	private String getTopic(String deviceId) {
		Assert.notNull(deviceId, "deviceId 不可以为空");
		return "v1/"+deviceId+"/device/control";
	}


}
