package com.zhidong.zhang.mqconverter.converter;

public interface MessageQueue {

    void sendMessage(String message);

    String receiveMessage(String topic);

    /**
     * 接收消息，然后转发到数据目的地
     * @param queue
     */
    void converterMq(MessageQueue queue);
}