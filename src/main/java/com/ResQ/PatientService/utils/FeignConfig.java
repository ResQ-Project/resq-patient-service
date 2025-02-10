package com.resq.PatientService.utils;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public OkHttpClient feignOkHttpClient() {
        return new OkHttpClient();
    }
}
