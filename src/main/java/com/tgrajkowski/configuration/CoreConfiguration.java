package com.tgrajkowski.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
public class CoreConfiguration {

    @Value("${nbp.api}")
    private String nbpApi;
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.rootUri(nbpApi).build();
    }

    @Bean
    public Clock applicationClock() {
        return new ApplicationClock();
    }
}
