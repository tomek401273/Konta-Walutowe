package com.tgrajkowski.model.dto;

import com.tgrajkowski.model.fund.CurrencyEnum;
import com.tgrajkowski.validator.CurrencyEnumValueValidation;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Builder
@Data
public class ExchangeDto {
    @NotBlank
    @CurrencyEnumValueValidation(currencyEnumClass = CurrencyEnum.class)
    private String currencyFrom;

    @NotBlank
    @CurrencyEnumValueValidation(currencyEnumClass = CurrencyEnum.class)
    private String currencyTo;

    @NotNull
    @Positive
    private BigDecimal amount;

    @PESEL
    private String pesel;
}
