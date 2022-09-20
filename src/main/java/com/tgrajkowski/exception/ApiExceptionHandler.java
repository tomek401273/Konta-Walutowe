package com.tgrajkowski.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException,
                                                               HttpHeaders httpHeaders,
                                                               HttpStatus httpStatus,
                                                               WebRequest webRequest){
        ErrorDetail errorDetail= new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Validation Problem");
        errorDetail.setDetail("Input validation is not correct");
        errorDetail.setDeveloperMessage(methodArgumentNotValidException.getClass().getName());
        List<FieldError> fieldErrorList = methodArgumentNotValidException.getBindingResult().getFieldErrors();
        for (FieldError fieldError: fieldErrorList) {
            List<ValidationException> validationExceptionList= errorDetail.getExceptions().get(fieldError.getField());
            if (validationExceptionList ==null){
                validationExceptionList= new ArrayList<>();
                errorDetail.getExceptions().putIfAbsent(fieldError.getField(), validationExceptionList);
            }
            ValidationException validationException= new ValidationException(fieldError.getCode(), fieldError.getDefaultMessage());
            validationExceptionList.add(validationException);
        }
        return handleExceptionInternal(methodArgumentNotValidException, errorDetail, httpHeaders, httpStatus, webRequest);
    }


}