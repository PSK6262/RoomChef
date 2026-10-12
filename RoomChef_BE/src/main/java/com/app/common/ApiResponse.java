package com.app.common;

import lombok.Getter;

/**
 * [API 표준 응답 래퍼 - ApiResponse]
 */
@Getter
public class ApiResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    private ApiResponse(ResultCode resultCode, String customMessage, T data) {
        this.code = resultCode.getCode();
        this.message = (customMessage != null && !customMessage.trim().isEmpty()) ? customMessage : resultCode.getMessage();
        this.data = data;
    }

    private ApiResponse(ResultCode resultCode, T data) {
        this(resultCode, null, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return response(ResultCode.SUCCESS, data);
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> error(ResultCode resultCode) {
        return response(resultCode, null);
    }

    public static <T> ApiResponse<T> error(ResultCode resultCode, String customMessage) {
        return new ApiResponse<>(resultCode, customMessage, null);
    }

    public static <T> ApiResponse<T> response(ResultCode resultCode, T data) {
        return new ApiResponse<>(resultCode, data);
    }

    public String getStatus() {
        return (code != null && code.startsWith("SUC")) ? "SUCCESS" : "FAIL";
    }
}
