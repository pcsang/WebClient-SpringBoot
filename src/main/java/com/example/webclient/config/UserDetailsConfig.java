package com.example.webclient.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "internal.credential")
@Data
public class UserDetailsConfig {
    private String userInfo;
    private String password;
}
