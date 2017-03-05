package org.horiga.study.glowroot.service;

import org.horiga.study.glowroot.controller.NotifyController.Message;

public interface NotifyHandler {

    void onMessage(Message message);
}
