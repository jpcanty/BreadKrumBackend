package com.bkb.restservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ApplicationConfiguration {

    public static final String ALPACA_OBJECT_MAPPER = "alpaca_object_mapper";
    public static final String BACK4APP_OBJECT_MAPPER = "back4app_object_mapper";
    public static final String SQLITE_RESPOSITORY = "sqlite_repository";
    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

    @Bean(ALPACA_OBJECT_MAPPER)
    public ObjectMapper alpacaObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        
        return mapper;
    }
    
    @Bean(BACK4APP_OBJECT_MAPPER)
    public ObjectMapper back4AppObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Primary
    @Bean
    public ObjectMapper defaultObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        
        return mapper;
    }
}
