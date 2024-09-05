package com.zhidong.zhang.mqconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zhangzhidong
 */
@EnableAsync
@SpringBootApplication
public class MqConverterApplication{
    public static void main(String[] args) {
        SpringApplication.run(MqConverterApplication.class, args);
    }

}
