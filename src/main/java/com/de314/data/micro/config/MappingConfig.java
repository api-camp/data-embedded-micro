package com.de314.data.micro.config;

import com.de314.data.local.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfig {

    @Bean
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JsonUtils jsonUtils(ObjectMapper jsonObjectMapper) {
        return new JsonUtils(jsonObjectMapper);
    }
}
