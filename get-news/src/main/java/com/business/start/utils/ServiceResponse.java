package com.business.start.utils;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse<R> {

    private int code;

    private String message;

    private R result;

    public static <V> ServiceResponse<V> ok(String value) {
        return ServiceResponse.<V>builder()
                .code(0)
                .message(value)
                .build();
    }

    public static <V> ServiceResponse<V> ok(V value) {
        return ServiceResponse.<V>builder()
                .code(0)
                .message("成功")
                .result(value)
                .build();
    }

    public static <V> ServiceResponse<V> sysTemError(String message) {
        return ServiceResponse.<V>builder()
                .code(-1)
                .message(message)
                .build();
    }

}
