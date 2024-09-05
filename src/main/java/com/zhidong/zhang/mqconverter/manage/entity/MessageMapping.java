package com.zhidong.zhang.mqconverter.manage.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 21:31
 */
@Data
@Entity
@Table(name = "message_mapping")
public class MessageMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "source_mq_config_id", foreignKey = @ForeignKey(name = "fk_source_mq_config"))
    private MqConfig sourceMqConfig;

    @ManyToOne
    @JoinColumn(name = "target_mq_config_id", foreignKey = @ForeignKey(name = "fk_target_mq_config"))
    private MqConfig targetMqConfig;

    // Assuming you'll use JSON or serialized string
    @Column(name = "conversion_rules", columnDefinition = "TEXT", nullable = false)
    private String conversionRules;
    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


}