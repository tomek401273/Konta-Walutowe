package com.tgrajkowski.controller;

import com.tgrajkowski.exception.DataConsistencyException;
import com.tgrajkowski.exception.ErrorDetail;
import com.tgrajkowski.model.account.AccountNotFoundException;
import com.tgrajkowski.model.dto.AccountDto;
import com.tgrajkowski.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @ApiOperation(value = "createAccount")
    @ApiResponses({
            @ApiResponse(code = 200, message = "0k", response = Long.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping
    public Long createAccount(@Valid @RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @GetMapping("{id}")
    public AccountDto getAccount(@ApiParam(value = "accountId", required = true, defaultValue = "112L") @PathVariable("id") Long id) {
        return accountService.getAccount(id);
    }

    @ExceptionHandler({DataConsistencyException.class, AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleDataConsistencyException(RuntimeException e) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setDetail(e.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

}
