package com.coffee.exception;

import com.coffee.domain.ErrorDetails;

public class ScraperException extends ExceptionBase {
    public ScraperException(ErrorDetails errorDetails) {
        super(errorDetails);
    }
}
