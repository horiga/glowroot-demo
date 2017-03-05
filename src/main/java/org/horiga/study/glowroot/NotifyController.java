package org.horiga.study.glowroot;

import java.util.concurrent.Callable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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

    @Getter
    @Builder
    public static class ErrorMessage implements ResponseMessage {
        @JsonProperty("error")
        private String error;
        @JsonProperty("error_description")
        private String errorDescription;
    }

    public static final ResponseMessage DEFAULT_SUCCESS = new ResponseMessage() {};

    @PostMapping("/event")
    public Callable<ResponseEntity<ResponseMessage>> onEvent(
            @Valid Message message,
            BindingResult bindings) throws Exception {
        return () -> ResponseEntity.ok(DEFAULT_SUCCESS);
    }
}
