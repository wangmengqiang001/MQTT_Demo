package com.testmock.springmock.process;

import org.springframework.stereotype.Component;

@Component
public class Gateway implements MqttGateway {

	@Override
	public boolean sendToMqtt(String payload) {
		// TODO Auto-generated method stub
		
		return true;

	}

	@Override
	public boolean sendToMqtt(String topic, String payload)  {
		// TODO Auto-generated method stub
		
		return false;

	}

}
