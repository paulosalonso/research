package com.github.paulosalonso.research.application.exceptionhandler;

import com.github.paulosalonso.research.application.exceptionhandler.Error.Field;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;

        var error = Error.builder()
                .status(status.value())
                .message("Requested resource not found")
                .build();

        return handleExceptionInternal(e, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = ex.getStatus();

        var error = Error.builder()
                .status(status.value())
                .message(ex.getReason())
                .build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {

        var status = HttpStatus.BAD_REQUEST;
        var message = "'%s' is an invalid value for the '%s' URL parameter. Required type is '%s'.";
        message = String.format(message, ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());

        var error = Error.builder()
                .status(status.value())
                .message(message)
                .build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    public ResponseEntity handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        var fields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Field.builder()
                        .name(error.getField())
                        .message(messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                        .build())
                .collect(toList());

        var error = Error.builder()
                .status(status.value())
                .message("Invalid field(s)")
                .fields(fields)
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(">>> ERROR <<<<", ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
