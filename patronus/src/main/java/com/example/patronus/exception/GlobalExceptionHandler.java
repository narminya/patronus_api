package com.example.patronus.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatusCode status,
                                                                     WebRequest request) {
        log.error(ex.getMessage(), ex);

        List<String> details = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        details.add(builder.toString());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorDetails(details)
                .message("Invalid JSON")
                .statusCode(status.value())
                .status(HttpStatus.valueOf(status.value()))
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error(ex.getMessage(), ex);


        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorDetails(details)
                .message("Malformed JSON request")
                .statusCode(status.value())
                .status(HttpStatus.valueOf(status.value()))
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error(ex.getMessage(), ex);

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getObjectName() + " : " + error.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorDetails(details)
                .message("Validation Errors")
                .statusCode(status.value())
                .status(HttpStatus.valueOf(status.value()))
                .build();

        return ResponseEntity.status(status).body(errorResponse);

    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error(ex.getMessage(), ex);

        List<String> details = new ArrayList<>();
        details.add(ex.getParameterName() + " parameter is missing");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorDetails(details)
                .message("Validation Errors")
                .statusCode(status.value())
                .status(HttpStatus.valueOf(status.value()))
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }


    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {

        log.error(exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(NotFoundException.STATUS.value())
                .status(NotFoundException.STATUS)
                .build();

        return ResponseEntity.status(NotFoundException.STATUS).body(errorResponse);
    }


//    @ExceptionHandler(AlreadyException.class)
//    protected ResponseEntity<Object> handleAlreadyException(AlreadyException exception) {
//
//        log.error(exception.getMessage(), exception);
//
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .message(exception.getMessage())
//                .statusCode(AlreadyException.STATUS.value())
//                .status(AlreadyException.STATUS)
//                .build();
//
//        return ResponseEntity.status(AlreadyException.STATUS).body(errorResponse);
//    }
//
//
//    @ExceptionHandler(ProcessException.class)
//    protected ResponseEntity<Object> handleProcessException(ProcessException exception) {
//
//        log.error(exception.getMessage(), exception);
//
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .message(exception.getMessage())
//                .statusCode(ProcessException.STATUS.value())
//                .status(ProcessException.STATUS)
//                .build();
//
//        return ResponseEntity.status(ProcessException.STATUS).body(errorResponse);
//    }
}

