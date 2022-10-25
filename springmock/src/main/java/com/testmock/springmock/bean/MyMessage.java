package com.testmock.springmock.bean;

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
public class MyMessage {
    private String topic;
    private String content;

}
