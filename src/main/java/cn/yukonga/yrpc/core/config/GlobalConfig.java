package cn.yukonga.yrpc.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: yukong
 * @date: 2018/12/29 17:06
 */
@Configuration
public class GlobalConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
