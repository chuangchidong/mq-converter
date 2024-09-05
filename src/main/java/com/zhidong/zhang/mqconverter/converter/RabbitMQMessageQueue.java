package com.zhidong.zhang.mqconverter.converter;

import com.zhidong.zhang.mqconverter.commons.BasicBase;
import com.zhidong.zhang.mqconverter.utils.ConvertUtils;

import java.util.function.Consumer;

public class RabbitMQMessageQueue implements MessageQueue {
    private final BasicBase base; // 这里应该包含连接RabbitMQ所需的所有信息

    public RabbitMQMessageQueue(BasicBase base) {
        this.base = base;
        // 初始化连接等（省略具体实现）
    }

    @Override
    public void sendMessage(String message) {
        // 实现发送消息到 RabbitMQ（省略具体实现）
        ConvertUtils.rabbitProducer(base, message);
    }

    @Override
    public String receiveMessage(String topic) {

        ConvertUtils.rabbitConsumer(base);
        // 实现从 RabbitMQ 接收消息（省略具体实现）
        return "Received message from RabbitMQ topic: " + topic;
    }

    @Override
    public void converterMq(MessageQueue queue) {
        Consumer<String> consumer = queue::sendMessage;
        ConvertUtils.rabbitConsumer(base, consumer);
    }


    public void closeConnection(BasicBase base){
        ConvertUtils.closeRabbitConnection(base);
        ConvertUtils.closeRabbitProducerChannel(base);
        ConvertUtils.closeRabbitProducerChannel(base);

        ConvertUtils.cleanRocketProducer(base);
        ConvertUtils.cleanRocketConsumer(base);
    }
}