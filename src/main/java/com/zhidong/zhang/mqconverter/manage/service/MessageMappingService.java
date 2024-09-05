package com.zhidong.zhang.mqconverter.manage.service;

import com.zhidong.zhang.mqconverter.commons.BasicBase;
import com.zhidong.zhang.mqconverter.converter.MessageQueue;
import com.zhidong.zhang.mqconverter.converter.MessageQueueFactory;
import com.zhidong.zhang.mqconverter.manage.dao.MessageMappingDao;
import com.zhidong.zhang.mqconverter.manage.entity.MessageMapping;
import com.zhidong.zhang.mqconverter.manage.entity.MqConfig;
import com.zhidong.zhang.mqconverter.utils.ConvertUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 21:43
 */

@Service
public class MessageMappingService {

    @Autowired
    private MessageMappingDao messageMappingDao;

    public List<MessageMapping> getAllMessageMappings() {
        return messageMappingDao.findAll();
    }

    public MessageMapping getMessageMappingById(Integer id) {
        return messageMappingDao.findById(id).orElse(null);
    }

    public MessageMapping saveMessageMapping(MessageMapping messageMapping) {
        return messageMappingDao.save(messageMapping);
    }

    public void deleteMessageMapping(Integer id) {
        messageMappingDao.deleteById(id);
    }

    public void startConverterById(Integer id) {
        MessageMapping mapping = this.getMessageMappingById(id);
        MqConfig targetConfig = mapping.getTargetMqConfig();
        MqConfig sourceConfig = mapping.getSourceMqConfig();

//        BasicBase source = basicBean(sourceConfig);
        MessageQueue source = MessageQueueFactory.createMessageQueue(sourceConfig.getMqType(), basicBean(sourceConfig));
        MessageQueue target = MessageQueueFactory.createMessageQueue(targetConfig.getMqType(), basicBean(targetConfig));

        source.converterMq(target);

    }

    private static @NotNull BasicBase basicBean(MqConfig sourceConfig) {
        BasicBase source = new  BasicBase();
        source.setHost(sourceConfig.getHost());
        source.setPort(sourceConfig.getPort());
        source.setUsername(sourceConfig.getUsername());
        source.setPassword(sourceConfig.getPassword());
        source.setTopic(sourceConfig.getTopic());
        source.setGroup(sourceConfig.getGroup());
        return source;
    }

    // You can add more business logic methods here if needed
}