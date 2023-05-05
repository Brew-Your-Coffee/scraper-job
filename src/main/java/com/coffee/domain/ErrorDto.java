package com.coffee.domain;

import java.io.Serial;
import java.io.Serializable;

public class ErrorDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 2556576895512144686L;

    private final String code;
    private final String message;

    public ErrorDto(ErrorDetails errorDetails) {
        this.code = errorDetails.getErrorCode();
        this.message = errorDetails.getMessage();
    }
}
