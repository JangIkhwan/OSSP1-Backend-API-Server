package com.example.cavityapiserver.common.exception_handler;

import com.example.cavityapiserver.common.exception.BadWebClientRequestException;
import com.example.cavityapiserver.common.response.BaseErrorResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class
BadWebClientRequestExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadWebClientRequestException.class)
    public BaseErrorResponse handle_BadWebClientException(BadWebClientRequestException e) {
        log.error("[handle_BadWebClientException]", e);
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }

}