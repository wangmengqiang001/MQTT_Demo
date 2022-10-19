package com.huawen.mqtt.dubbo;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.huawen.mqtt.bean.MyMessage;
import com.huawen.mqtt.controller.MqttPublishController;

@Service(version = "1.0.0") // dubbo的service注解，不具备spring的@service注解的功能
@Component
public class MqttPublisherService implements MqttPublisherDubbo {
	
	@Autowired
	MqttPublishController controler;

	@Override
	public String send(MyMessage myMessage) {
		
		return controler.send(myMessage);
	}

	@Override
	public String send(String topic, String message) {
		
		return controler.send(topic, message);
	}

}
