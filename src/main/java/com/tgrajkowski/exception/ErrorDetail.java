package com.tgrajkowski.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorDetail {
    private String title;
    private int status;
    private String detail;
    private long timestamp;
    private Map<String, List<ValidationException>> exceptions = new HashMap<>();
    private String developerMessage;

}