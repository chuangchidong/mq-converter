package com.zhidong.zhang.mqconverter.manage.dao;

import com.zhidong.zhang.mqconverter.manage.entity.MessageMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 21:36
 */
public interface MessageMappingDao extends JpaRepository<MessageMapping, Integer> {
    // Custom queries can be added here if needed
}