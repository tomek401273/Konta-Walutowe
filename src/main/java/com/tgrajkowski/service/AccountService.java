package com.tgrajkowski.service;

import com.tgrajkowski.constant.Constants;
import com.tgrajkowski.exception.DataConsistencyException;
import com.tgrajkowski.model.account.Account;
import com.tgrajkowski.model.account.AccountConverter;
import com.tgrajkowski.model.account.AccountDao;
import com.tgrajkowski.model.account.AccountNotFoundException;
import com.tgrajkowski.model.dto.AccountDto;
import com.tgrajkowski.model.fund.CurrencyEnum;
import com.tgrajkowski.model.fund.Fund;
import com.tgrajkowski.validator.AgeValidator;
import com.tgrajkowski.validator.OneAccountForPersonValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountDao accountDao;
    private final AccountConverter accountConverter;
    private AgeValidator ageValidator;

    private OneAccountForPersonValidator oneAccountForPersonValidator;

    public Long createAccount(AccountDto accountDto) {
        Account account = accountConverter.mapToAccount(accountDto);

        if (!ageValidator.isPersonAdult(accountDto)) {
            throw new DataConsistencyException(Constants.PERSON_IS_NOT_ADULT);
        }
        oneAccountForPersonValidator.validate(accountDto);

        account.getFundList().add(Fund.builder()
                .currencyEnum(CurrencyEnum.PLN)
                .amount(accountDto.getAmountInPLN())
                .account(account)
                .build());

        account.getFundList().add(Fund.builder()
                .currencyEnum(CurrencyEnum.USD)
                .amount(BigDecimal.ZERO)
                .account(account)
                .build());

        accountDao.save(account);
        return account.getId();
    }

    public AccountDto getAccount(Long id) {
        Account account = accountDao.findAccountWithFunds(id);
        if (account != null) {
            return accountConverter.mapToAccountDto(account);
        } else {
            throw new AccountNotFoundException(Constants.ACCOUNT_NOT_FOUND);
        }
    }

    public Account getAccountByPesel(String pesel) {
        return accountDao.findAccountByPesel(pesel);
    }
}
