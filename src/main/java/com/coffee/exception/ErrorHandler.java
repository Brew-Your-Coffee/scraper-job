package com.coffee.exception;

import com.coffee.domain.ErrorDetails;
import com.coffee.domain.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {ExceptionBase.class})
    public ResponseEntity<ErrorDto> handleException(ExceptionBase ex) {
        ErrorDetails details = ex.getErrorDetails();
        ErrorDto errorResponse = new ErrorDto(details);
        return new ResponseEntity<>(errorResponse, details.getResponseStatus());
    }

}
