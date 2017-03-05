package org.horiga.study.glowroot.service;

import org.horiga.study.glowroot.controller.NotifyController.Message;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Component
@Slf4j
public class NotifyHandlerImpl implements NotifyHandler {

    private final OkHttpClient client;

    public NotifyHandlerImpl(OkHttpClient client) {
        this.client = client;
    }

    public void onMessage(Message message) {
        log.info("onMessage", message);
    }
}
