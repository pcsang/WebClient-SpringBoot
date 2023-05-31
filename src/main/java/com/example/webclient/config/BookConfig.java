package com.example.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BookConfig {
    @Bean
    public WebClient webClientBook() {
        return WebClient.builder().build();
    }
}
