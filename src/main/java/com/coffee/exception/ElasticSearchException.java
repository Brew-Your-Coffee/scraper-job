package com.coffee.exception;

import com.coffee.domain.ErrorDetails;

public class ElasticSearchException extends ExceptionBase {
    public ElasticSearchException(ErrorDetails errorDetails) {
        super(errorDetails);
    }
}
