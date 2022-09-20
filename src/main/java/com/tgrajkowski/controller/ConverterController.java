package com.tgrajkowski.controller;

import com.tgrajkowski.exception.DataConsistencyException;
import com.tgrajkowski.exception.ErrorDetail;
import com.tgrajkowski.model.account.AccountNotFoundException;
import com.tgrajkowski.model.dto.ExchangeDto;
import com.tgrajkowski.service.ConverterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/convert")
public class ConverterController {
    private final ConverterService converterService;

    @PostMapping
    public void exchange(@Valid @RequestBody ExchangeDto exchangeDto) {
        converterService.convert(exchangeDto);
    }

    @ExceptionHandler({DataConsistencyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleDataConsistencyException(RuntimeException e) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setDetail(e.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
