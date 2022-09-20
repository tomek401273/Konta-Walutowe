package com.tgrajkowski.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgrajkowski.constant.Constants;
import com.tgrajkowski.model.account.Account;
import com.tgrajkowski.model.account.AccountDao;
import com.tgrajkowski.model.dto.ExchangeDto;
import com.tgrajkowski.model.fund.CurrencyEnum;
import com.tgrajkowski.model.fund.Fund;
import com.tgrajkowski.model.nbp.Rate;
import com.tgrajkowski.model.nbp.RatesDao;
import com.tgrajkowski.service.AccountService;
import com.tgrajkowski.service.ConverterService;
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
import java.util.ArrayList;
import java.util.Comparator;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class ConverterServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ConverterService converterService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RatesDao ratesDao;


    @Test
    public void shouldExchangePLNToUSD() throws Exception {
        // given
        String pesel = "81012189633";
        Account account = Account.builder()
                .name("tomek")
                .surname("thomson")
                .pesel(pesel)
                .fundList(new ArrayList<>())
                .build();
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.PLN)
                .amount(BigDecimal.valueOf(100)).account(account).build());
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.USD)
                .amount(BigDecimal.ZERO).account(account).build());
        accountDao.save(account);

        ratesDao.deleteAll();
        ratesDao.save(Rate.builder().code("PLN").mid(BigDecimal.ONE).build());
        ratesDao.save(Rate.builder().code("USD").mid(BigDecimal.valueOf(4.0)).build());


        ExchangeDto exchangeDto = ExchangeDto.builder()
                .pesel(pesel)
                .currencyFrom(CurrencyEnum.PLN.getName())
                .currencyTo(CurrencyEnum.USD.getName())
                .amount(BigDecimal.ONE)
                .build();

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exchangeDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        // then
        Account accountFromDb = accountDao.findAccountWithFunds(account.getId());
        accountFromDb.getFundList().sort(Comparator.comparing(Fund::getCurrencyEnum, Comparator.reverseOrder()));

        Assertions.assertThat(accountFromDb.getFundList().get(0).getCurrencyEnum().getName()).isEqualTo(CurrencyEnum.PLN.getName());
        Assertions.assertThat(accountFromDb.getFundList().get(0).getAmount()).isEqualTo(BigDecimal.valueOf(99.00).setScale(2, RoundingMode.HALF_UP));
        Assertions.assertThat(accountFromDb.getFundList().get(1).getCurrencyEnum().getName()).isEqualTo(CurrencyEnum.USD.getName());
        Assertions.assertThat(accountFromDb.getFundList().get(1).getAmount()).isEqualTo(BigDecimal.valueOf(0.25).setScale(2, RoundingMode.HALF_UP));

        // clean
        accountDao.deleteAll();
        ratesDao.deleteAll();
    }

    @Test
    public void shouldExchangeUSDToPLN() throws Exception {
        // given
        String pesel = "81012189633";
        Account account = Account.builder()
                .name("tomek")
                .surname("thomson")
                .pesel(pesel)
                .fundList(new ArrayList<>())
                .build();
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.PLN).amount(BigDecimal.ZERO).account(account).build());
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.USD).amount(BigDecimal.valueOf(100.00)).account(account).build());
        accountDao.save(account);

        ratesDao.deleteAll();
        ratesDao.save(Rate.builder().code("PLN").mid(BigDecimal.ONE).build());
        ratesDao.save(Rate.builder().code("USD").mid(BigDecimal.valueOf(4.0)).build());

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .pesel(pesel)
                .currencyFrom(CurrencyEnum.USD.getName())
                .currencyTo(CurrencyEnum.PLN.getName())
                .amount(BigDecimal.ONE)
                .build();

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exchangeDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        // then
        Account accountFromDb = accountDao.findAccountWithFunds(account.getId());
        accountFromDb.getFundList().sort(Comparator.comparing(Fund::getCurrencyEnum, Comparator.reverseOrder()));

        Assertions.assertThat(accountFromDb.getFundList().get(0).getCurrencyEnum().getName()).isEqualTo(CurrencyEnum.PLN.getName());
        Assertions.assertThat(accountFromDb.getFundList().get(0).getAmount()).isEqualTo(BigDecimal.valueOf(4).setScale(2, RoundingMode.HALF_UP));
        Assertions.assertThat(accountFromDb.getFundList().get(1).getCurrencyEnum().getName()).isEqualTo(CurrencyEnum.USD.getName());
        Assertions.assertThat(accountFromDb.getFundList().get(1).getAmount()).isEqualTo(BigDecimal.valueOf(99).setScale(2, RoundingMode.HALF_UP));

        // clean
        accountDao.deleteAll();
        ratesDao.deleteAll();
    }


    @Test
    public void shouldNotExchange_accountNotExists() throws Exception {
        // given
        String pesel = "81012189633";

        ratesDao.deleteAll();
        ratesDao.save(Rate.builder().code("PLN").mid(BigDecimal.ONE).build());
        ratesDao.save(Rate.builder().code("USD").mid(BigDecimal.valueOf(4.0)).build());

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .pesel(pesel)
                .currencyFrom(CurrencyEnum.USD.getName())
                .currencyTo(CurrencyEnum.PLN.getName())
                .amount(BigDecimal.ONE)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exchangeDto)))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(Constants.ACCOUNT_NOT_EXISTS_IN_DB));

        // clean
        accountDao.deleteAll();
        ratesDao.deleteAll();
    }

    @Test
    public void shouldNotExchange_NotEnoughMoney() throws Exception {
        // given
        String pesel = "81012189633";
        Account account = Account.builder()
                .name("tomek")
                .surname("thomson")
                .pesel(pesel)
                .fundList(new ArrayList<>())
                .build();
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.PLN).amount(BigDecimal.ZERO).account(account).build());
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.USD).amount(BigDecimal.ZERO).account(account).build());
        accountDao.save(account);

        ratesDao.deleteAll();
        ratesDao.save(Rate.builder().code("PLN").mid(BigDecimal.ONE).build());
        ratesDao.save(Rate.builder().code("USD").mid(BigDecimal.valueOf(4.0)).build());

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .pesel(pesel)
                .currencyFrom(CurrencyEnum.USD.getName())
                .currencyTo(CurrencyEnum.PLN.getName())
                .amount(BigDecimal.ONE)
                .build();

        // then
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exchangeDto)))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(Constants.ACCOUNT_NOT_ENOUGH_MONEY + exchangeDto.getCurrencyFrom()));

        // clean
        accountDao.deleteAll();
        ratesDao.deleteAll();
    }

    @Test
    public void shouldNotExchange_CurrencyNotInPermittedValues() throws Exception {
        // given
        String pesel = "81012189633";
        Account account = Account.builder()
                .name("tomek")
                .surname("thomson")
                .pesel(pesel)
                .fundList(new ArrayList<>())
                .build();
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.PLN).amount(BigDecimal.ONE).account(account).build());
        account.getFundList().add(Fund.builder().currencyEnum(CurrencyEnum.USD).amount(BigDecimal.ZERO).account(account).build());
        accountDao.save(account);

        ratesDao.deleteAll();
        ratesDao.save(Rate.builder().code("PLN").mid(BigDecimal.ONE).build());
        ratesDao.save(Rate.builder().code("USD").mid(BigDecimal.valueOf(4.0)).build());

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .pesel(pesel)
                .currencyFrom(CurrencyEnum.USD.getName())
                .currencyTo(CurrencyEnum.PLN.getName())
                .amount(BigDecimal.ONE)
                .build();

        // then
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"currencyFrom\": \"PLN\",\n" +
                                "    \"currencyTo\": \"EUR\",\n" +
                                "    \"amount\": 1,\n" +
                                "    \"pesel\": \"81012189633\"\n" +
                                "}"))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.exceptions.currencyTo[0].message").value(Constants.NOT_OPERATED_CURRENCY));

        // clean
        accountDao.deleteAll();
        ratesDao.deleteAll();
    }

}