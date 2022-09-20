package com.tgrajkowski.model.account;

import com.tgrajkowski.model.fund.CurrencyEnum;
import com.tgrajkowski.model.fund.Fund;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "amount_in_pln")
    private BigDecimal amountInPLN;

    @OneToMany(targetEntity = Fund.class, mappedBy = "account", cascade = CascadeType.ALL)
    private List<Fund> fundList = new ArrayList<>();

    public Fund getFundByCurrency(CurrencyEnum currencyEnum) {
        return fundList.stream().filter(fund -> fund.getCurrencyEnum()
                        .equals(currencyEnum)).findFirst()
                .orElseThrow(() -> new RuntimeException("Currency not found"));
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", pesel='" + pesel + '\'' +
                ", amountInPLN=" + amountInPLN +
                '}';
    }
}

