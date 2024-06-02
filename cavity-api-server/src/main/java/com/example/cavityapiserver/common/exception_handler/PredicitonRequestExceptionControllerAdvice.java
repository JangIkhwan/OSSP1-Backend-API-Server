package com.example.cavityapiserver.common.exception_handler;

import com.example.cavityapiserver.common.exception.PredictionRequestException;
import com.example.cavityapiserver.common.response.BaseErrorResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@Slf4j
@Priority(0)
@RestControllerAdvice
public class PredicitonRequestExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PredictionRequestException.class)
    public BaseErrorResponse handle_UserException(PredictionRequestException e) {
        log.error("[handle_UserException]", e);
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }

}
