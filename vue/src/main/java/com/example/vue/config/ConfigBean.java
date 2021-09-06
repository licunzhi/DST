package com.example.vue.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ConfigBean
 * @Description 系统配置类
 * @Author lcz
 * @Date 2019/09/05 17:34
 */
@ConfigurationProperties(prefix = "prefix", ignoreInvalidFields = true)
@Data
@Component
public class ConfigBean {

    private int readTimeout;

    private int connectTimeout;
}
