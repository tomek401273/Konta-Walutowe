package com.tgrajkowski.model.nbp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@Entity
@Table(name = "rates")
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "code")
    private String code;

    @Column(name = "mid")
    private BigDecimal mid;
}
