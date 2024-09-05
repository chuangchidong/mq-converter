package com.zhidong.zhang.mqconverter.rocket;

/**
 * 描述: 消费者
 *
 * @author zhangzhidong
 * @date 2024-03-31 08:20
 */
public interface RocketMQConsumer {
    void subscribe(String topic, String tag);

    void start();

    void shutdown();
}