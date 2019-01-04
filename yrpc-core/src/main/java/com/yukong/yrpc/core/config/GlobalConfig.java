package com.yukong.yrpc.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author: yukong
 * @date: 2018/12/29 17:06
 */
@PropertySource(value = {"classpath:yRpc.properties"})
@Configuration
public class GlobalConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
