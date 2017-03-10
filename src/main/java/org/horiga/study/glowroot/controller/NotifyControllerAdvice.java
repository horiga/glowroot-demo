package org.horiga.study.glowroot.controller;

import org.horiga.study.glowroot.controller.NotifyController.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@RestControllerAdvice
public class NotifyControllerAdvice {

    @Getter
    @Builder
    public static class ErrorMessage extends ResponseMessage {
        @JsonProperty("error")
        private String error;
        @JsonProperty("error_description")
        private String errorDescription;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage badRequest(IllegalArgumentException ex) {
        return ErrorMessage.builder()
                           .error("illegal_argument")
                           .errorDescription(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage badRequest(Exception ex) {
        return ErrorMessage.builder()
                           .error("unknown")
                           .errorDescription(ex.getMessage())
                           .build();
    }
}
