package com.zhidong.zhang.mqconverter.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * 描述: 作为rabbitmq的消费者
 *
 * @author zhangzhidong
 * @date 2024-03-30 19:03
 */
@Slf4j
public class MyMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        log.info("处理消息：{}", message.toString());
        log.info("解析消息：{}", new String(message.getBody(), StandardCharsets.UTF_8));
    }
}

