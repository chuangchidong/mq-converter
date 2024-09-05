package com.zhidong.zhang.mqconverter.converter;

import com.zhidong.zhang.mqconverter.commons.BasicBase;
import com.zhidong.zhang.mqconverter.utils.ConvertUtils;

import java.util.function.Consumer;

public class RocketMQMessageQueue implements MessageQueue {
    private final BasicBase base; // 这里应该包含连接RocketMQ所需的所有信息

    public RocketMQMessageQueue(BasicBase base) {
        this.base = base;
        // 初始化连接等（省略具体实现）
    }

    @Override
    public void sendMessage(String message) {
        ConvertUtils.rocketProducer(base, message);
    }

    @Override
    public String receiveMessage(String topic) {
        // 实现从 RocketMQ 接收消息（省略具体实现）
        ConvertUtils.rocketConsumer(base);
        return "Received message from RocketMQ topic: " + topic;
    }

    @Override
    public void converterMq(MessageQueue queue) {
        Consumer<String> consumer = queue::sendMessage;
        ConvertUtils.rocketConsumer(base, consumer);
    }
}