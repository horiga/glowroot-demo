package org.horiga.study.glowroot;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @ConfigurationProperties("myapp")
    @Component
    @Data
    static class Properties {

        private String endpoint = "https://notify-api.line.me";

        @NotNull
        private String accessToken;

        private int maxWorkerThreadPoolSize = 1000;

        @NestedConfigurationProperty
        private Http http;

        @SuppressWarnings("WeakerAccess")
        @Data
        public static class Http {
            int connectTimeout = 3000;
            int readTimeout = 3000;
            Boolean retryOnConnectionFailure = Boolean.FALSE;
            Boolean followRedirects = Boolean.FALSE;
        }
    }

    @Configuration
    static class MyWebMvcConfig extends WebMvcConfigurerAdapter {

        private final Properties properties;

        MyWebMvcConfig(Properties properties) {
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

        @Override
        public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
            configurer.setTaskExecutor(threadPoolAsyncTaskExecutor());
        }

        @Bean
        AsyncTaskExecutor threadPoolAsyncTaskExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setMaxPoolSize(properties.getMaxWorkerThreadPoolSize());
            executor.setThreadNamePrefix("async-worker-");
            return executor;
        }
    }

}
