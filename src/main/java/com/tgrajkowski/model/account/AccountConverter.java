package com.tgrajkowski.model.account;

import com.tgrajkowski.model.dto.AccountDto;
import com.tgrajkowski.model.dto.FundDto;
import com.tgrajkowski.model.fund.Fund;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountConverter {
    public Account mapToAccount(AccountDto accountDto) {
        return Account.builder()
                .name(accountDto.getName())
                .surname(accountDto.getSurname())
                .pesel(accountDto.getPesel())
                .amountInPLN(accountDto.getAmountInPLN())
                .fundList(new ArrayList<>())
                .build();
    }

    public AccountDto mapToAccountDto(Account account) {
        List<FundDto> fundDtoList = account.getFundList().stream().map(this::mapToFund).collect(Collectors.toList());

        return AccountDto.builder()
                .name(account.getName())
                .surname(account.getSurname())
                .pesel(account.getPesel())
                .amountInPLN(account.getAmountInPLN())
                .fundList(fundDtoList)
                .build();
    }

    private FundDto mapToFund(Fund fund) {
        return FundDto.builder()
                .amount(fund.getAmount())
                .currencyEnum(fund.getCurrencyEnum())
                .build();
    }
}
