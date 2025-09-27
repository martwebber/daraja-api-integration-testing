package com.mpesa.sampleapi.config;

import io.github.openpaydev.mpesa.MpesaClient;
import io.github.openpaydev.mpesa.auth.MpesaTokenManager;
import io.github.openpaydev.mpesa.core.MpesaConfig;
import io.github.openpaydev.mpesa.core.MpesaEnvironment;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MpesaConfiguration {

    private final ConfigProperties configProperties;

    public MpesaConfiguration(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public MpesaConfig mpesaConfig() {
        return MpesaConfig.builder()
                .consumerKey(configProperties.getConsumerKey())
                .consumerSecret(configProperties.getConsumerSecret())
                .businessShortCode(configProperties.getBusinessShortCode())
                .passKey(configProperties.getPasskey())
                .environment(
                        "production".equalsIgnoreCase(configProperties.getEnvironment())
                                ? MpesaEnvironment.PRODUCTION
                                : MpesaEnvironment.SANDBOX
                )
                .build();
    }

    @Bean
    public MpesaTokenManager mpesaTokenManager(MpesaConfig mpesaConfig, OkHttpClient okHttpClient) {
        return new MpesaTokenManager(mpesaConfig, okHttpClient);
    }

    @Bean
    public MpesaClient mpesaClient(MpesaConfig mpesaConfig, MpesaTokenManager mpesaTokenManager, OkHttpClient okHttpClient) {
        return new MpesaClient(mpesaConfig, mpesaTokenManager, okHttpClient);
    }
}