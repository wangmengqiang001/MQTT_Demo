package com.huawen.mqtt.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:xjl
 * @date:2022/5/6 9:33
 * @Description: 消息实体对象
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMessage implements Serializable{
    private String topic;
    private String content;

}
