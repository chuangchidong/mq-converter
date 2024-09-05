package com.zhidong.zhang.mqconverter.manage.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述: mq配置
 *
 * @author zhangzhidong
 * @date 2024-03-31 20:56
 */
@Data
@Entity
@Table(name = "mq_config")
public class MqConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "mq_type", nullable = false)
    private String mqType;

    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private Integer port;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "topic")
    private String topic;

    @Column(name = "group")
    private String group;

    @Column(name = "other_configs", columnDefinition = "TEXT")
    private String otherConfigs;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

}