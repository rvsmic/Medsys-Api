package com.medsys.medsysapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableScheduling
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Scheduled(fixedRate = 1000 * 60)
    public void deleteExpiredTokens() {
        secUserDetailsService.deleteExpiredTokens();
    }

}
