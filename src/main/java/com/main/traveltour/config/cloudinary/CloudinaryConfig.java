package com.main.traveltour.config.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private final String CLOUD_NAME = "daawxyvyj";

    private final String API_KEY = "231836774534317";

    private final String API_SECRET = "6-8C8JEdalJNuZjYXYJKGLS5fzo";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
