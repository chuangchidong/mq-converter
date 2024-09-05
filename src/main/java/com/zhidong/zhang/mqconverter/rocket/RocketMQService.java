package com.zhidong.zhang.mqconverter.rocket;

import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 08:53
 */
public class RocketMQService {

    public void sendMessage( String nameServerAddr,String producerGroup, String topic,String tag, String message) {
        RocketMQProducer producer = null;
        try {
            producer = new DynamicRocketMQProducer(producerGroup, nameServerAddr);
            producer.sendMessage(topic, tag, message);
        } catch (MQClientException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(producer)) {
                producer.shutdown();
            }
        }
    }
}
