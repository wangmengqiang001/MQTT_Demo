package com.testmock.springmock.process;



public interface MqttGateway {
	
    boolean sendToMqtt(String payload);

 
    boolean sendToMqtt(String topic, String payload) throws Exception;

}
