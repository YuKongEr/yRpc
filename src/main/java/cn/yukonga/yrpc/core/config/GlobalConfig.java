package cn.yukonga.yrpc.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : yukong
  */
@Configuration
public class GlobalConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
