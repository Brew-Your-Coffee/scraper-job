package com.coffee.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ErrorDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = -2350079822484567777L;

    private String errorCode;
    private String message;
    private HttpStatus responseStatus;
}
