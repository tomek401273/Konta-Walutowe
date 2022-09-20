package com.tgrajkowski.validator;

import com.tgrajkowski.constant.Constants;
import com.tgrajkowski.exception.DataConsistencyException;
import com.tgrajkowski.model.account.AccountDao;
import com.tgrajkowski.model.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OneAccountForPersonValidator {
    private final AccountDao accountDao;

    public void validate(AccountDto accountDto) {
        if (accountDao.existsByPesel(accountDto.getPesel())){
            throw new DataConsistencyException(Constants.PERSON_ALREADY_HAS_ACCOUNT);
        }
    }
}