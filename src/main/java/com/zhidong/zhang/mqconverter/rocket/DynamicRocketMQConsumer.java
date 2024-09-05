package com.zhidong.zhang.mqconverter.rocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 08:23
 */
@Slf4j
public class DynamicRocketMQConsumer implements RocketMQConsumer {
    private DefaultMQPushConsumer consumer;

    public DynamicRocketMQConsumer(String consumerGroup, String nameServerAddr) {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServerAddr);
        // set consumer consume message from now
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
        consumer.setAllocateMessageQueueStrategy(new AllocateMessageQueueAveragely());
    }
    //"CHECKSTYLE:OFF"
//    /** 分配策略 */
//    static AllocateMessageQueueStrategy allocateMessageQueueStrategy = new AllocateMessageQueueStrategy() {
//        @Override
//        public List<MessageQueue> allocate(String consumerGroup, String currentCID, List<MessageQueue> queueList,
//                                           List<String> cidAll) {
//
//            String allocateStrategy = SpringActiveProfileUtil.getBean("nacosProperties", NacosProperties.class)
//                    .getMqAllocateStrategy();
//            List<MessageQueue> list = new ArrayList<>();
//            Integer strategyInteger = Integer.valueOf(allocateStrategy);
//            list.add(queueList.get(strategyInteger));
//            return list;
//        }
//
//        @Override
//        public String getName() {
//            return "divide_by_queue";
//        }
//    };

    @Override
    public void subscribe(String topic, String tag) {
        try {
            consumer.subscribe(topic, tag != null ? tag : "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        MessageListenerConcurrently messageListener =  (List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            for (MessageExt msg : msgs) {
                try {
                    // 将消息体从字节数组解码为字符串
                    String messageBody = new String(msg.getBody(), StandardCharsets.UTF_8);
                    log.info("{} DynamicRocketMQConsumer Received Message: {}", Thread.currentThread().getName(), messageBody);
                    log.info("queue info queueOffset:{}",msg.getQueueOffset());
                    MessageQueue messageQueue = context.getMessageQueue();

                    log.info("context topic:{}, message:{}",messageQueue.getTopic(),messageQueue.toString());
                } catch (Exception e) {
                    log.error("Error parsing message body", e);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        };
        consumer.registerMessageListener(messageListener);
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        consumer.shutdown();
    }
}