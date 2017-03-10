package org.horiga.study.glowroot.controller;

import java.util.HashMap;
import java.util.concurrent.Callable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.horiga.study.glowroot.service.NotifyHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class NotifyController {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ResponseMessage<T> {

        public static final String SUCCESS_VALUE = "success";

        private String status;
        private T content;

        static ResponseMessage success() {
            return new ResponseMessage<Object>(SUCCESS_VALUE, new HashMap<String, Object>(0));
        }

        static <T> ResponseMessage success(T content) {
            return new ResponseMessage<Object>(SUCCESS_VALUE, content);
        }
    }

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

    private final NotifyHandler handler;

    NotifyController(NotifyHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/api/event")
    public Callable<ResponseMessage> onEvent(
            @Valid @RequestBody Message message) throws Exception {
        return () -> {
            handler.onMessage(message);
            return ResponseMessage.success();
        };
    }
}
