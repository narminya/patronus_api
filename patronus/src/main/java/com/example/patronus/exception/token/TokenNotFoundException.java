package com.example.patronus.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException() {
        super("The specified Refresh Token is not found!");
    }
}