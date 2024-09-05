package com.zhidong.zhang.mqconverter.controller;

import cn.hutool.core.util.RandomUtil;
import com.zhidong.zhang.mqconverter.commons.BasicBase;
import com.zhidong.zhang.mqconverter.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 描述: 数据方配置
 *
 * @author zhangzhidong
 * @date 2024-03-29 07:42
 */
@Slf4j
@RestController
public class ConfigController {

    /**
     * RabbitMq工厂
     *
     * @param base
     * @return
     */
    @PostMapping("/rabbit-producer")
    public ResponseEntity<String> rabbitProducer(@RequestBody BasicBase base) {

        String message = "messageBody-测试:" + RandomUtil.randomNumbers(4);
//        ConvertUtils.rabbitSpringProducer(base, message);
        ConvertUtils.rabbitProducer(base, message);
        return ResponseEntity.ok("Rabbit生产者，创建消息message: " + message);
    }

    @PostMapping("/rabbit-consumer")
    public ResponseEntity<String> rabbitConsumer(@RequestBody BasicBase base) {
        try {
//            ConvertUtils.rabbitSpringConsumer(base);
            ConvertUtils.rabbitConsumer(base);
            String routingKey = base.getTopic();
            return ResponseEntity.ok("Consumer created and listening on topic(routingKey): " + routingKey);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to create consumer: " + e.getMessage());
        }
    }


    @PostMapping("/rocket-producer")
    public ResponseEntity<String> createRocketProducer(@RequestBody BasicBase base) {
        String message = "这是个rocketmq生产者测试" + RandomUtil.randomNumbers(4);
        ConvertUtils.rocketProducer(base, message);
        return ResponseEntity.ok("Rabbit生产者，创建消息topic: " + message);
    }

    /**
     * producerGroup 和 topic要唯一，订阅的topic要和指定的保持一致
     * @param base
     * @return
     */
    @PostMapping("/rocket-consumer")
    public ResponseEntity<String> createRocketConsumer(@RequestBody BasicBase base) {
        ConvertUtils.rocketConsumer(base);
        String topic = base.getTopic();
        return ResponseEntity.ok("Consumer created and listening on topic(routingKey): " + topic);
    }

    @PostMapping("/rocket-clean-producer")
    public ResponseEntity<String> rocketCleanProducer(@RequestBody BasicBase base) {
        ConvertUtils.cleanRocketProducer(base);
        String topic = base.getTopic();
        return ResponseEntity.ok("清除RocketMQ生产者对象: " + topic);
    }

    @PostMapping("/rocket-clean-consumer")
    public ResponseEntity<String> rocketCleanConsumer(@RequestBody BasicBase base) {
        ConvertUtils.cleanRocketConsumer(base);
        String topic = base.getTopic();
        return ResponseEntity.ok("清除RocketMQ消费者对象: " + topic);
    }

    @PostMapping("/rabbit-close-connect")
    public ResponseEntity<String> rabbiCloseConnect(@RequestBody BasicBase base) {
//        ConvertUtils.closeRabbitConnection(base);
        ConvertUtils.closeRabbitConnection(base);
        String topic = base.getTopic();
        return ResponseEntity.ok("关闭rabbitmq的连接: " + topic);
    }

    @PostMapping("/rabbit-close-producer")
    public ResponseEntity<String> closeRabbitProducerChannel(@RequestBody BasicBase base) {
//        ConvertUtils.closeRabbitProducerChannel(base);
        ConvertUtils.closeRabbitProducerChannel(base);
        String topic = base.getTopic();
        return ResponseEntity.ok("关闭rabbitmq的生产者channel: " + topic);
    }

    @PostMapping("/rabbit-close-consumer")
    public ResponseEntity<String> closeRabbitConsumerChannel(@RequestBody BasicBase base) {
//        ConvertUtils.closeRabbitConsumerChannel(base);
        ConvertUtils.closeRabbitConsumerChannel(base);
        String topic = base.getTopic();
        return ResponseEntity.ok("关闭rabbitmq的消费者channel: " + topic);
    }



}
