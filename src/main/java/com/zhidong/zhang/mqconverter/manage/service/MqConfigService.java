package com.zhidong.zhang.mqconverter.manage.service;

import com.zhidong.zhang.mqconverter.manage.dao.MqConfigDao;
import com.zhidong.zhang.mqconverter.manage.entity.MqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 21:40
 */
@Service
public class MqConfigService {

    @Autowired
    private MqConfigDao mqConfigDao;

    public List<MqConfig> getAllMqConfigs() {
        return mqConfigDao.findAll();
    }

    public MqConfig getMqConfigById(Integer id) {
        return mqConfigDao.findById(id).orElse(null);
    }

    public MqConfig saveMqConfig(MqConfig mqConfig) {
        return mqConfigDao.save(mqConfig);
    }

    public void deleteMqConfig(Integer id) {
        mqConfigDao.deleteById(id);
    }

}
