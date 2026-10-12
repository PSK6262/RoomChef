package com.app.common;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
    private final ResultCode resultCode;

    public ExternalApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ExternalApiException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }
}
