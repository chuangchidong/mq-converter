package com.zhidong.zhang.mqconverter.rocket;

/**
 * 描述: 生产者
 *
 * @author zhangzhidong
 * @date 2024-03-31 08:20
 */
public interface RocketMQProducer {
    void sendMessage(String topic,String tag, String message);
    void shutdown();
}