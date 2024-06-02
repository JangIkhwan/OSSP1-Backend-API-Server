package com.example.cavityapiserver.common.exception;

import com.example.cavityapiserver.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class PredictionRequestException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public PredictionRequestException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public PredictionRequestException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}
