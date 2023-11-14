package com.example.auth.global.exception.handler;

import com.example.auth.global.exception.*;
import com.example.auth.global.response.LmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClientException.class)
    protected LmsResponse<String> handleClientException(
            ClientException ex) {
        String errorMessage = ex.getMessage();
        return new LmsResponse<>(HttpStatus.BAD_REQUEST, "","CLIENT-EXCEPTION" , errorMessage, LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    protected LmsResponse<String> handleNotFondException(
            NotFoundException ex) {
        String errorMessage = ex.getMessage();
        return new LmsResponse<>(HttpStatus.NOT_FOUND, "","NOTFOUND-EXCEPTION" , errorMessage, LocalDateTime.now());
    }

    @ExceptionHandler(DuplicateException.class)
    protected LmsResponse<String> handleDuplicateException(
            DuplicateException ex) {
        String errorMessage = ex.getMessage();
        return new LmsResponse<>(HttpStatus.BAD_REQUEST, "","DUPLICATE-EXCEPTION" , errorMessage, LocalDateTime.now());
    }

    @ExceptionHandler(MethodException.class)
    protected LmsResponse<String> handleMethodException(
            MethodException ex) {
        String errorMessage = ex.getMessage();
        return new LmsResponse<>(HttpStatus.BAD_REQUEST, "","METHOD-EXCEPTION" , errorMessage, LocalDateTime.now());
    }

    @ExceptionHandler(JwtException.class)
    protected LmsResponse<String> handleJwtException(
            JwtException ex) {
        String errorMessage = ex.getMessage();
        return new LmsResponse<>(HttpStatus.BAD_REQUEST, "","JWT-EXCEPTION" , errorMessage, LocalDateTime.now());
    }



}