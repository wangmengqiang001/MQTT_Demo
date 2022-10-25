package com.huawen.mqtt.controller;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ResponseMessageListener implements IMqttMessageListener {

	private Object wakeSignal;
	
	public ResponseMessageListener(Object wakeSignal) {
		super();
		this.wakeSignal = wakeSignal;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try {
		      System.out.println("topic: " + topic);
		      synchronized(wakeSignal) {
		    	  wakeSignal.notify();
		      }
		      
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}

}
