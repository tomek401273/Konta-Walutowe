package com.tgrajkowski.model.currency;

import com.tgrajkowski.model.nbp.Rate;
import com.tgrajkowski.model.nbp.RateDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyMapper {

    public List<Rate> mapToRateList(List<RateDto> rateDtos) {
        return rateDtos.stream()
                .map(this::mapToRate)
                .collect(Collectors.toList());
    }

    private Rate mapToRate(RateDto rateDto) {
        return Rate.builder()
                .code(rateDto.getCode())
                .currency(rateDto.getCurrency())
                .mid(rateDto.getMid())
                .build();
    }
}
