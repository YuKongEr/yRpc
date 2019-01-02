package com.yukong.yrpc.core.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: yukong
 * @date: 2018/12/29 15:59
 */
@Component
public class JsonParse implements Parse {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public <T> byte[] serialize(T obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("error",e);
        }
        return null;
    }
    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> cls) {
        try {
            return objectMapper.readValue(bytes, cls);
        } catch (IOException e) {
            logger.error("error",e);
        }
        return null;
    }

}
