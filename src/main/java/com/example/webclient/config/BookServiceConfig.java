package com.example.webclient.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "internal.book-service")
@Data
public class BookServiceConfig {
    private String typeHttp;
    private String hostname;
    private String port;
    private String url;
}
