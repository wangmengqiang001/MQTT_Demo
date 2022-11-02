package com.huawen.mqtt.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huawen.mqtt.bean.MyMessage;

@FeignClient(name="mqtt-messager",url = "http://localhost:${server.port}/")
public interface MqttPublisherRest {

	@RequestMapping(value="/send", method = RequestMethod.POST)
	String send(@RequestBody MyMessage myMessage);

	@RequestMapping(value = "/send",method= RequestMethod.GET)
	String send(@RequestParam("topic") String topic, @RequestParam("message") String message);

}