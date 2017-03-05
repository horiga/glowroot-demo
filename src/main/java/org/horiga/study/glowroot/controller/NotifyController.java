package org.horiga.study.glowroot.controller;

import java.util.concurrent.Callable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.horiga.study.glowroot.service.NotifyHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class NotifyController {

    public interface ResponseMessage {}

    @Data
    public static class Message {

        @SuppressWarnings("unused")
        public interface Type {
            String NONE = "none";
            String ALERT = "alert";
            String EVENT = "event";
        }

        @SuppressWarnings("unused")
        public interface Priority {
            String HIGH = "high";
            String MEDIUM = "medium";
            String LOW = "low";
        }

        @JsonProperty("project")
        @NotNull
        private String project;

        @JsonProperty("node_id")
        @NotNull
        private String nodeId;

        @JsonProperty("message")
        @NotEmpty
        @Size(min = 1, max = 1000, message = "must be at 1-1000 characters.")
        private String message;

        @JsonProperty("hostname")
        private String hostname;

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("type")
        private String type = Type.NONE;

        @JsonProperty("priority")
        private String priority = Priority.MEDIUM;
    }

    public static final ResponseMessage DEFAULT_SUCCESS = new ResponseMessage() {};

    private final NotifyHandler handler;

    NotifyController(NotifyHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/event")
    public Callable<ResponseEntity<ResponseMessage>> onEvent(
            @Valid Message message) throws Exception {
        return () -> {
            handler.onMessage(message);
            return ResponseEntity.ok(DEFAULT_SUCCESS);
        };
    }
}
