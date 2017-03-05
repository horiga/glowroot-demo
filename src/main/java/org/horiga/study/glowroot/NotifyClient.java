package org.horiga.study.glowroot;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Component
@Slf4j
public class NotifyClient {

    private final OkHttpClient client;

    public NotifyClient(OkHttpClient client) {
        this.client = client;
    }

    public void execute() {

        // do something

    }
}
