package com.zhidong.zhang.mqconverter.manage.controller;

/**
 * 描述:
 *
 * @author zhangzhidong
 * @date 2024-03-31 21:49
 */

import com.zhidong.zhang.mqconverter.manage.dto.MqConfigDto;
import com.zhidong.zhang.mqconverter.manage.entity.MqConfig;
import com.zhidong.zhang.mqconverter.manage.service.MessageMappingService;
import com.zhidong.zhang.mqconverter.manage.service.MqConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mq-config")
public class MqConfigController {

    @Autowired
    private MqConfigService mqConfigService;

    @GetMapping
    public ResponseEntity<List<MqConfig>> getAllMqConfigs() {
        List<MqConfig> mqConfigs = mqConfigService.getAllMqConfigs();
        return ResponseEntity.ok(mqConfigs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MqConfig> getMqConfigById(@PathVariable Integer id) {
        MqConfig mqConfig = mqConfigService.getMqConfigById(id);
        if (mqConfig == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mqConfig);
    }

    @PostMapping
    public ResponseEntity<MqConfig> saveMqConfig(@RequestBody MqConfig mqConfig) {
        MqConfig savedMqConfig = mqConfigService.saveMqConfig(mqConfig);
        return ResponseEntity.created(URI.create("/api/mq-config/" + savedMqConfig.getId())).body(savedMqConfig);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMqConfig(@PathVariable Integer id) {
        mqConfigService.deleteMqConfig(id);
        return ResponseEntity.noContent().build();
    }

    // Custom method for Vue.js front-end
    @GetMapping("/table-data")
    public ResponseEntity<List<MqConfigDto>> getMqConfigTableData() {
        List<MqConfig> mqConfigs = mqConfigService.getAllMqConfigs();
        List<MqConfigDto> mqConfigDtos = mqConfigs.stream()
                .map(mqConfig -> new MqConfigDto(mqConfig.getId(), mqConfig.getHost(), mqConfig.getPort()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(mqConfigDtos);
    }

    @Resource
    private MessageMappingService messageMappingService;

    @GetMapping("/start-converter")
    public ResponseEntity<String> startConverter(Integer id) {
        messageMappingService.startConverterById(id);
        return ResponseEntity.ok("转化完成");
    }
}