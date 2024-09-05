package com.zhidong.zhang.mqconverter.rocket;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 08:21
 */
public class DynamicRocketMQProducer implements RocketMQProducer {
    private DefaultMQProducer producer;

    public DynamicRocketMQProducer(String producerGroup, String nameServerAddr) throws MQClientException {
        producer = new DefaultMQProducer();
        producer.setNamesrvAddr(nameServerAddr);
        producer.setProducerGroup(producerGroup);
        producer.start();
    }

    @Override
    public void sendMessage(String topic,String tag, String message) {
        Message msg = new Message(topic, tag, message.getBytes());
        try {
            producer.send(msg);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        producer.shutdown();
    }
}
