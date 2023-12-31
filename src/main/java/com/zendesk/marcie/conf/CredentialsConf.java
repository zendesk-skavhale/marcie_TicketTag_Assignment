package com.zendesk.marcie.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CredentialsConf {
    @Bean
    public String apiUsername(@Value("${zendesk.api.username}") String username) {
        return username;
    }

    @Bean
    public String apiPassword(@Value("${zendesk.api.password}") String password) {
        return password;
    }
}
