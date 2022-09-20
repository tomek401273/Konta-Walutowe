package com.tgrajkowski.service;

import com.tgrajkowski.constant.Constants;
import com.tgrajkowski.exception.DataConsistencyException;
import com.tgrajkowski.model.account.Account;
import com.tgrajkowski.model.dto.ExchangeDto;
import com.tgrajkowski.model.fund.CurrencyEnum;
import com.tgrajkowski.model.fund.Fund;
import com.tgrajkowski.model.nbp.RatesDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ConverterService {
    private final AccountService accountService;
    private final RatesDao ratesDao;

    @Transactional
    public void convert(ExchangeDto exchangeDto) {
        Account account = accountService.getAccountByPesel(exchangeDto.getPesel());
        if (Objects.isNull(account)) {
            throw new DataConsistencyException(Constants.ACCOUNT_NOT_EXISTS_IN_DB);
        }
        Fund fundFrom = account.getFundByCurrency(CurrencyEnum.valueOf(exchangeDto.getCurrencyFrom()));
        Fund fundTo = account.getFundByCurrency(CurrencyEnum.valueOf(exchangeDto.getCurrencyTo()));
        fundFrom.setAmount(fundFrom.getAmount().subtract(exchangeDto.getAmount()));

        if (fundFrom.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new DataConsistencyException(Constants.ACCOUNT_NOT_ENOUGH_MONEY + exchangeDto.getCurrencyFrom());
        }

        BigDecimal rateFrom = getRate(CurrencyEnum.valueOf(exchangeDto.getCurrencyFrom()));
        BigDecimal resultFrom = exchangeDto.getAmount().multiply(rateFrom);

        BigDecimal rateTo = getRate(CurrencyEnum.valueOf(exchangeDto.getCurrencyTo()));
        BigDecimal resultTo = resultFrom.divide(rateTo, RoundingMode.HALF_UP);

        fundTo.setAmount(fundTo.getAmount().add(resultTo));
    }

    public BigDecimal getRate(CurrencyEnum currencyEnum) {
        return ratesDao.findByCode(currencyEnum.getName()).getMid();
    }
}
