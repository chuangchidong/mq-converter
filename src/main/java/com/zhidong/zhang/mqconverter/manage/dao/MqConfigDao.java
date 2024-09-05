package com.zhidong.zhang.mqconverter.manage.dao;

import com.zhidong.zhang.mqconverter.manage.entity.MqConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 21:39
 */

public interface MqConfigDao extends JpaRepository<MqConfig, Integer> {
    // Custom queries can be added here if needed
}