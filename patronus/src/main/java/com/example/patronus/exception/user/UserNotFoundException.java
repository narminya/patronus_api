package com.example.patronus.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("User %s is not found".formatted(username));
    }

    public UserNotFoundException() {
        super("User is not found");
    }
}