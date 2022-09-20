package com.tgrajkowski.model.dto;

import com.tgrajkowski.model.fund.CurrencyEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class FundDto {
    private CurrencyEnum currencyEnum;
    private BigDecimal amount;
}
