package com.tgrajkowski.validator;

import com.tgrajkowski.exception.DataConsistencyException;
import com.tgrajkowski.model.account.Account;
import com.tgrajkowski.model.account.AccountDao;
import com.tgrajkowski.model.dto.AccountDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class OneAccountForPersonValidatorTest {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private OneAccountForPersonValidator oneAccountForPersonValidator;

    @Test
    void shouldThrowException_PersonExistInDb() {
        // given
        Account account = Account.builder()
                .name("tomek")
                .surname("thomson")
                .pesel("123567")
                .build();
        accountDao.save(account);

        // when & then
        Assertions.assertThatThrownBy(() -> oneAccountForPersonValidator
                        .validate(AccountDto.builder().pesel(account.getPesel()).build()))
                .isInstanceOf(DataConsistencyException.class)
                .hasMessage("Person with this number of PESEL already has an account.");
    }

    @Test
    void shouldNotThrowException_PersonNotExistInDb() {
        // given & when & then
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                () -> oneAccountForPersonValidator
                        .validate(AccountDto.builder().pesel("12356708").build()));
    }
}