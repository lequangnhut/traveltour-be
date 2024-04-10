package com.main.traveltour.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.main.traveltour.utils.LocalTimeDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomLocalTimeDeserialization() {
        return builder -> {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
            builder.modules(simpleModule);
        };
    }
}

