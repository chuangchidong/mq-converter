//package com.zhidong.zhang.mqconverter.utils;
//
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.StrUtil;
//import com.rabbitmq.client.*;
//import com.zhidong.zhang.mqconverter.commons.BasicBase;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.common.UtilAll;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.Connection;
//import org.springframework.amqp.rabbit.connection.ConnectionFactoryUtils;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@Slf4j
//public class ConvertSpringUtils {
//
//    /**
//     * 获取一个新的字符串，当参数routingKey以key结尾时，将参数转为以queue结尾，当参数未以key结尾时，返回结果在原参数后添加 _queue作为结尾
//     *
//     * @param routingKey
//     * @return
//     */
//    public static String toQueue(String routingKey) {
//        if (routingKey.endsWith("key")) {
//            return routingKey.replace("key", "queue");
//        }
//        return routingKey + "_queue";
//    }
//
//    private static Map<String, Channel> rabbitChannelMap = MapUtil.newHashMap();
//    private static Map<String, Connection> rabbitConnectionMap = MapUtil.newHashMap();
//
//    /**
//     * rabbit生产者
//     *
//     * @param base
//     * @param message
//     */
//    public static void rabbitSpringProducer(BasicBase base, String message) {
//
//        String host = base.getHost();
//        int port = base.getPort();
//        String username = base.getUsername();
//        String password = base.getPassword();
//
//        String exchange = base.getGroup();
//        String routingKey = base.getTopic();
//        String queue = toQueue(routingKey);
//
//        try {
//            CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//            connectionFactory.setHost(host);
//            connectionFactory.setPort(port);
//            connectionFactory.setUsername(username);
//            connectionFactory.setPassword(password);
//            String key = host + "@@" + port + "@@" + username + "@@" + password;
//            Connection connection = rabbitConnectionMap.get(key);
//
//            if (Objects.isNull(connection)) {
//                connection = ConnectionFactoryUtils.createConnection(connectionFactory, true);
//                rabbitConnectionMap.put(key, connection);
//            }
//            String channelKey = key + "@@" + exchange + "@@" + routingKey + "@@Producer";
//            Channel channel = rabbitChannelMap.get(channelKey);
//
//            if (Objects.isNull(channel)) {
//                channel = connection.createChannel(false);
//                rabbitChannelMap.put(channelKey, channel);
//            }
//            // 声明一个direct类型的exchange
//            channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true);
//            // 声明queue（如果尚未存在）
//            channel.queueDeclare(queue, true, false, false, null);
//            // 绑定queue到exchange上，使用routing key "routingKey"
//            channel.queueBind(queue, exchange, routingKey);
//
//            channel.basicPublish(exchange, routingKey, null, message.getBytes());
//            log.info("rabbitmq 生产者message:{},key:{}", message, channelKey);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * rabbit生产者
//     *
//     * @param base
//     */
//    public static void rabbitSpringConsumer(BasicBase base) {
//
//        String host = base.getHost();
//        int port = base.getPort();
//        String username = base.getUsername();
//        String password = base.getPassword();
//
//        String exchange = base.getGroup();
//        String routingKey = base.getTopic();
//        String queue = toQueue(routingKey);
//
//        try {
//            CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//            connectionFactory.setHost(host);
//            connectionFactory.setPort(port);
//            connectionFactory.setUsername(username);
//            connectionFactory.setPassword(password);
//            String key = host + "@@" + port + "@@" + username + "@@" + password;
//            Connection connection = rabbitConnectionMap.get(key);
//            if (Objects.isNull(connection)) {
//                connection = ConnectionFactoryUtils.createConnection(connectionFactory, true);
//                rabbitConnectionMap.put(key, connection);
//            }
//            String channelKey = key + "@@" + exchange + "@@" + routingKey + "@@Consumer";
//            Channel channel = rabbitChannelMap.get(channelKey);
//            if (Objects.isNull(channel)) {
//                channel = connection.createChannel(false);
//                rabbitChannelMap.put(channelKey, channel);
//            }
//
//            // 声明一个direct类型的exchange
//            channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true);
//            // 声明queue（如果尚未存在）
//            channel.queueDeclare(queue, true, false, false, null);
//            // 绑定queue到exchange上，使用routing key "routingKey"
//            channel.queueBind(queue, exchange, routingKey);
////            channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> log.info("接收到的信息 exchange:{},rountingKey:{},message:{}", exchange1, routingKey1, new String(body)));
//
//            DefaultConsumer consumer = new DefaultConsumer(channel) {
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    log.info("接收到的信息 exchange:{},rountingKey:{},message:{}", envelope.getExchange(), envelope.getRoutingKey(), new String(body));
//                }
//            };
//            channel.setDefaultConsumer(consumer);
//            channel.basicConsume(queue, true, consumer);
//
//
//            log.info("rabbitmq消费者key{}", channelKey);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public static void closeRabbitConnection(BasicBase base) {
//        String host = base.getHost();
//        int port = base.getPort();
//        String username = base.getUsername();
//        String password = base.getPassword();
//        try {
//            CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//            connectionFactory.setHost(host);
//            connectionFactory.setPort(port);
//            connectionFactory.setUsername(username);
//            connectionFactory.setPassword(password);
//            String key = host + "@@" + port + "@@" + username + "@@" + password;
//            Connection connection = rabbitConnectionMap.get(key);
//            if (Objects.nonNull(connection)) {
//                if (connection.isOpen()) {
//                    connection.close();
//                    connection = null;
//                    rabbitConnectionMap.remove(key);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public static void closeRabbitConsumerChannel(BasicBase base) {
//        String host = base.getHost();
//        int port = base.getPort();
//        String username = base.getUsername();
//        String password = base.getPassword();
//        String exchange = base.getGroup();
//        String routingKey = base.getTopic();
//        try {
//            String key = host + "@@" + port + "@@" + username + "@@" + password;
//            String channelKey = key + "@@" + exchange + "@@" + routingKey + "@@Consumer";
//            Channel channel = rabbitChannelMap.get(channelKey);
//
//            if (Objects.nonNull(channel)) {
//                if (channel.isOpen()) {
//                    channel.clearReturnListeners();
//                    channel.clearConfirmListeners();
//                    Consumer consumer = channel.getDefaultConsumer();
//                    if (Objects.nonNull(consumer)) {
//                        log.info(consumer.toString());
//                    }
//                    channel.close();
////                    channel = null;
////                    rabbitChannelMap.remove(channelKey);
//                    log.info("关闭rabbitmq的消费者channel，key:{}", channelKey);
//                } else {
//                    log.info("rabbitmq的消费者channel，已经关闭 key:{}", channelKey);
//                }
//            } else {
//                log.info("rabbitmq的消费者channel，已经为空 key:{}", channelKey);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void closeRabbitProducerChannel(BasicBase base) {
//        String host = base.getHost();
//        int port = base.getPort();
//        String username = base.getUsername();
//        String password = base.getPassword();
//        String exchange = base.getGroup();
//        String routingKey = base.getTopic();
//        try {
//            String key = host + "@@" + port + "@@" + username + "@@" + password;
//            String channelKey = key + "@@" + exchange + "@@" + routingKey + "@@Producer";
//            Channel channel = rabbitChannelMap.get(channelKey);
//
//            if (Objects.nonNull(channel)) {
//                if (channel.isOpen()) {
//                    channel.close();
//                    channel = null;
//                    rabbitChannelMap.remove(channelKey);
//                    log.info("关闭rabbitmq的生产者channel，key:{}", channelKey);
//                } else {
//                    log.info("rabbitmq的生产者channel，已经关闭 key:{}", channelKey);
//                }
//            }else {
//                log.info("rabbitmq的生产者channel，已经为空 key:{}", channelKey);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    /****RocketMQ操作方法****/
//    private static final Map<String, DefaultMQProducer> rocketProducerMap = new HashMap<>();
//    private static final Map<String, DefaultMQPushConsumer> rocketConsumerMap = new HashMap<>();
//
//    public static void rocketProducer(BasicBase base, String message) {
//        try {
//            String host = base.getHost();
//            int port = base.getPort();
//            String group = base.getGroup();
//            String topic = base.getTopic();
//
//            group = StrUtil.replace(group, ".", "_");
//            topic = StrUtil.replace(topic, ".", "_");
//
//            String nameServerAddr = host + ":" + port;
//            String key = nameServerAddr + "@@" + group + "@@" + topic;
//            DefaultMQProducer producer = rocketProducerMap.get(key);
//
//            if (Objects.isNull(producer)) {
//                producer = new DefaultMQProducer();
//                producer.setNamesrvAddr(nameServerAddr);
//                producer.setProducerGroup(group);
//
//                producer.start();
//                rocketProducerMap.put(key, producer);
//            }
//
//            org.apache.rocketmq.common.message.Message msg = new org.apache.rocketmq.common.message.Message(topic, null, message.getBytes());
//            producer.send(msg);
//            log.info("rocketmq 创建了一条消息， topic: {}, message:{}", topic, message);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void rocketConsumer(BasicBase base) {
//
//        String nameServerAddr = base.getHost() + ":" + base.getPort();
//        String topic = base.getTopic();
//        String group = base.getGroup();
//
//        group = StrUtil.replace(group, ".", "_");
//        topic = StrUtil.replace(topic, ".", "_");
//        String key = nameServerAddr + "@@" + group + "@@" + topic;
//        try {
//            DefaultMQPushConsumer consumer = rocketConsumerMap.get(key);
//            if (Objects.nonNull(consumer)) {
//                return;
//            }
//            consumer = new DefaultMQPushConsumer();
//            consumer.setNamesrvAddr(nameServerAddr);
//            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
//            consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
//            consumer.setAllocateMessageQueueStrategy(new AllocateMessageQueueAveragely());
//            consumer.setConsumerGroup(group);
//            consumer.subscribe(topic, "*");
//
//            MessageListenerConcurrently messageListener = (msgs, context) -> {
//                for (MessageExt msg : msgs) {
//                    try {
//                        // 将消息体从字节数组解码为字符串
//                        String messageBody = new String(msg.getBody(), StandardCharsets.UTF_8);
//                        log.info("RocketMQ消费者的消息体信息:{}", messageBody);
//                    } catch (Exception e) {
//                        assert log != null;
//                        log.error("Error parsing message body", e);
//                    }
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            };
//            consumer.registerMessageListener(messageListener);
//            consumer.start();
//            rocketConsumerMap.put(key, consumer);
//        } catch (Exception e) {
//            log.error("异常", e);
//        }
//    }
//
//    public static void cleanRocketProducer(BasicBase base) {
//
//        String host = base.getHost();
//        int port = base.getPort();
//        String group = base.getGroup();
//        String topic = base.getTopic();
//
//        group = StrUtil.replace(group, ".", "_");
//        topic = StrUtil.replace(topic, ".", "_");
//
//        String nameServerAddr = host + ":" + port;
//        String key = nameServerAddr + "@@" + group + "@@" + topic;
//        DefaultMQProducer producer = rocketProducerMap.get(key);
//
//        if (Objects.nonNull(producer)) {
//            producer.shutdown();
//            rocketProducerMap.remove(key);
//        }
//
//        log.info("rocketmq 清除生产者对象: {}", key);
//    }
//
//    public static void cleanRocketConsumer(BasicBase base) {
//
//        String host = base.getHost();
//        int port = base.getPort();
//        String group = base.getGroup();
//        String topic = base.getTopic();
//
//        group = StrUtil.replace(group, ".", "_");
//        topic = StrUtil.replace(topic, ".", "_");
//
//        String nameServerAddr = host + ":" + port;
//        String key = nameServerAddr + "@@" + group + "@@" + topic;
//        DefaultMQPushConsumer consumer = rocketConsumerMap.get(key);
//
//        if (Objects.nonNull(consumer)) {
//            consumer.shutdown();
//            rocketConsumerMap.remove(key);
//        }
//
//        log.info("rocketmq 清除消费者对象: {}", key);
//    }
//
//}
