create database mq_converter;
use mq_converter;


CREATE TABLE `mq_config` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '配置ID，主键',
  `mq_type` VARCHAR(50) NOT NULL COMMENT 'MQ类型，如RabbitMQ、RocketMQ等',
  `host` VARCHAR(255) NOT NULL COMMENT 'MQ服务器地址',
  `port` INT NOT NULL COMMENT 'MQ服务器端口',
  `username` VARCHAR(100) COMMENT 'MQ连接用户名',
  `password` VARCHAR(100) COMMENT 'MQ连接密码',
  `other_configs` TEXT COMMENT '其他MQ配置信息，可以存储为JSON或序列化字符串',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MQ配置信息表';


CREATE TABLE `message_mapping` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '映射ID，主键',
  `source_mq_config_id` INT NOT NULL COMMENT '源MQ配置ID',
  `target_mq_config_id` INT NOT NULL COMMENT '目标MQ配置ID',
  `conversion_rules` TEXT NOT NULL COMMENT '消息转换规则，存储为JSON或序列化字符串',
  `enabled` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否启用此映射关系',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`source_mq_config_id`) REFERENCES `mq_config`(`id`) ,
  FOREIGN KEY (`target_mq_config_id`) REFERENCES `mq_config`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息映射关系表';


ALTER TABLE `mq_config`
ADD UNIQUE INDEX `unique_mq_config` (`mq_type`, `host`, `port`);

CREATE INDEX `idx_source_mq_config_id` ON `message_mapping` (`source_mq_config_id`);
CREATE INDEX `idx_target_mq_config_id` ON `message_mapping` (`target_mq_config_id`);