package com.huawen.mqtt.controller;

import com.huawen.mqtt.bean.MyMessage;
import com.huawen.mqtt.inter.MqttGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:xjl
 * @date:2022/5/6 9:17
 * @Description: mqtt发布消息controller
 **/
@RestController
public class MqttPublishController {
    @Resource
    private MqttGateway mqttGateWay;

    @PostMapping("/send")
    public String send(@RequestBody MyMessage myMessage) {
        // 发送消息到指定主题
        mqttGateWay.sendToMqtt(myMessage.getTopic(), 1, myMessage.getContent());
        return "send topic: " + myMessage.getTopic() + ", message : " + myMessage.getContent();
    }
}
