package com.tgrajkowski.model.currency;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency {
    private String code;
    private BigDecimal value;
    private String name;
}
