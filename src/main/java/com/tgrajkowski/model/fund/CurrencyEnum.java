package com.tgrajkowski.model.fund;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CurrencyEnum {
    USD("USD"),
    PLN("PLN");

    @Getter
    private final String name;
}
