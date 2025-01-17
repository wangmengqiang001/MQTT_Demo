package com.huawen.mqtt.controller;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ResponseCallback implements MqttCallback {
	
	private Object wakeSignal;
	

	public ResponseCallback(Object wakeSignal) {
		super();
		this.wakeSignal = wakeSignal;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		
		
		//notify the message is received
		wakeSignal.notify();

	}

}
