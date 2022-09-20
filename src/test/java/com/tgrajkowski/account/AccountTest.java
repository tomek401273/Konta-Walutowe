package com.tgrajkowski.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgrajkowski.constant.Constants;
import com.tgrajkowski.model.account.Account;
import com.tgrajkowski.model.account.AccountConverter;
import com.tgrajkowski.model.account.AccountDao;
import com.tgrajkowski.model.dto.AccountDto;
import com.tgrajkowski.model.fund.CurrencyEnum;
import com.tgrajkowski.model.fund.Fund;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AccountTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountConverter accountConverter;

    @Test
    void shouldCreateAccount() throws Exception {
        // given
        AccountDto accountDto = AccountDto.builder()
                .name("tomek")
                .surname("thomson")
                .pesel("81012189633")
                .amountInPLN(BigDecimal.valueOf(100))
                .build();
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accountDto)))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber());
        Long accountId = Long.valueOf(resultActions.andReturn().getResponse().getContentAsString());
        Account accountFromDb = accountDao.findAccountWithFunds(accountId);
        Assertions.assertThat(accountFromDb).isNotNull();
        Assertions.assertThat(accountFromDb.getFundList()).isNotEmpty();
        accountFromDb.getFundList().sort(Comparator.comparing(Fund::getCurrencyEnum, Comparator.reverseOrder()));
        Assertions.assertThat(accountFromDb.getFundList().get(0).getCurrencyEnum().getName()).isEqualTo(CurrencyEnum.PLN.getName());
        Assertions.assertThat(accountFromDb.getFundList().get(0).getAmount()).isEqualTo(BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP));
        Assertions.assertThat(accountFromDb.getFundList().get(1).getCurrencyEnum().getName()).isEqualTo(CurrencyEnum.USD.getName());
        Assertions.assertThat(accountFromDb.getFundList().get(1).getAmount()).isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

        // clean
        accountDao.deleteAll();
    }

    @Test
    void shouldNotCreateAccount_IsNotAdult() throws Exception {
        // given
        AccountDto accountDto = AccountDto.builder()
                .name("tomek")
                .surname("thomson")
                .pesel("10320156756")
                .amountInPLN(BigDecimal.valueOf(100))
                .build();
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accountDto)))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(Constants.PERSON_IS_NOT_ADULT));
    }

    @Test
    void shouldNotCreateAccount_PersonExistInDb() throws Exception {
        // given
        AccountDto accountDto = AccountDto.builder()
                .name("tomek")
                .surname("thomson")
                .pesel("81012189633")
                .amountInPLN(BigDecimal.valueOf(100))
                .build();
        Account account = accountConverter.mapToAccount(accountDto);
        accountDao.save(account);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accountDto)))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(Constants.PERSON_ALREADY_HAS_ACCOUNT));

        // clean
        accountDao.deleteAll();
    }

    @Test
    void shouldGetAccount() throws Exception {
        // given
        Account account = Account.builder()
                .name("tomek")
                .surname("thomson")
                .pesel("123567")
                .build();
        accountDao.save(account);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/account/" + account.getId()))
                .andDo(MockMvcResultHandlers.print());
        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(account.getSurname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pesel").value(account.getPesel()));

        accountDao.deleteAll();
    }

    @Test
    void shouldNotGetAccount_AccountNotExists() throws Exception {
        // given & when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/account/-1"))
                .andDo(MockMvcResultHandlers.print());
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(Constants.ACCOUNT_NOT_FOUND));
    }

}
