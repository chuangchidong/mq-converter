package com.zhidong.zhang.mqconverter.commons;

import lombok.Data;

/**
 * 描述: 消息连接基础信息
 *
 * @author zhangzhidong
 * @date 2024-03-29 07:37
 */
@Data
public class BasicBase {

    private String host;
    private int port;
    private String username;
    private String password;

    private String topic;
    private String group;
}
