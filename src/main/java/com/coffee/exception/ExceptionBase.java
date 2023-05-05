package com.coffee.exception;

import com.coffee.domain.ErrorDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExceptionBase extends RuntimeException {
    private final ErrorDetails errorDetails;
}
