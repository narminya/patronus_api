package com.example.patronus.exception.stream;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StreamNotFoundException extends RuntimeException {

    public StreamNotFoundException(UUID id) {
        super(String.format("No stream associated with %s", id));
    }

}