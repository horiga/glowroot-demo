package org.horiga.study.glowroot;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;
import okhttp3.OkHttpClient;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @ConfigurationProperties("myapp")
    @Component
    @Data
    static class Properties {

        String endpoint = "https://notify-api.line.me";

        @NotNull
        String accessToken;

        @NestedConfigurationProperty
        Http http;

        @Data
        static class Http {
            int connectTimeout = 3000;
            int readTimeout = 3000;
            Boolean retryOnConnectionFailure = Boolean.FALSE;
            Boolean followRedirects = Boolean.FALSE;
        }
    }

    @Configuration
    static class Config {

        private final Properties properties;

        Config(Properties properties) {
            this.properties = properties;
        }

        @Bean
        OkHttpClient okHttpClient() {
            return new OkHttpClient.Builder()
                    .connectTimeout(properties.http.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(properties.http.getReadTimeout(), TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(properties.http.getRetryOnConnectionFailure())
                    .followRedirects(properties.http.getFollowRedirects())
                    .build();
        }
    }

}
