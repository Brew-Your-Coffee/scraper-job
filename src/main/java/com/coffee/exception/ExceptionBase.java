package com.coffee.exception;

import com.coffee.domain.ErrorDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExceptionBase extends Exception {
    private final ErrorDetails errorDetails;
}
