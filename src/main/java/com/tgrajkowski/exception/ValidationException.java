package com.tgrajkowski.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationException {
    private String code;
    private String message;
}