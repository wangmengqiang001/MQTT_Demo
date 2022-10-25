package com.huawen.mqtt.dubbo;

import com.huawen.mqtt.bean.MyMessage;

//import com.huawen.mqtt.controller.MqttPublisherRest;

public interface MqttPublisherDubbo {
	
	String send( MyMessage myMessage);

	
	String send( String topic, String message);

}
