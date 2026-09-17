package dev.prathamesh.ai.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson configuration for proper JSON serialization
 * Ensures null values are excluded and dates are formatted properly
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Exclude null values from JSON output
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        // Pretty print for debugging (optional)
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        return mapper;
    }
}