package com.zhidong.zhang.mqconverter.manage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 21:52
 */
@Data
@AllArgsConstructor
public class MqConfigDto {

    private Long id;
    private String host;
    private int port;
}
