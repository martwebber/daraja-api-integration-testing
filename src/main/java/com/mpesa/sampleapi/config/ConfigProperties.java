package com.mpesa.sampleapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Maps properties from application.properties to a configuration object.
 * We only need the core credentials; the SDK handles the rest.
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "mpesa")
public class ConfigProperties {

    private String consumerKey;
    private String consumerSecret;
    private String businessShortCode;
    private String passkey;
    private String callbackUrl;
    private String environment;

}