package com.zhidong.zhang.mqconverter.converter;

import com.zhidong.zhang.mqconverter.commons.BasicBase;

public class MessageQueueFactory {

    public static MessageQueue createMessageQueue(String type, BasicBase base) {
        switch (type.toLowerCase()) {
            case "rabbitmq":
                return new RabbitMQMessageQueue(base);
            case "rocketmq":
                return new RocketMQMessageQueue(base);
            default:
                throw new IllegalArgumentException("Unsupported message queue type: " + type);
        }
    }
}