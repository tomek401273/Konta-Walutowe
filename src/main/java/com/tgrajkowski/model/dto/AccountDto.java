package com.tgrajkowski.model.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class AccountDto {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @PESEL
    private String pesel;

    @NotNull
    @Positive
    private BigDecimal amountInPLN;

    private List<FundDto> fundList;
}
