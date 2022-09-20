package com.tgrajkowski.model.fund;

import com.tgrajkowski.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@Entity
@Table(name = "funds")
@NoArgsConstructor
@AllArgsConstructor
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fund_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyEnum currencyEnum;

    @Column(name = "amount", scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @Override
    public String toString() {
        return "Fund{" +
                "id=" + id +
                ", currency=" + currencyEnum +
                ", amount=" + amount +
                '}';
    }
}
