package com.tgrajkowski.service;

import com.tgrajkowski.model.nbp.CurrenciesNBP;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@RequiredArgsConstructor
@Service
public class RetrieveDataFromNBPApi {

    private final RestTemplate restTemplate;
    @Setter
    @Value("${nbp.api.endpoint.exchange.rates}")
    private String endpointExchangeRates;

    public CurrenciesNBP[] getData() {
        return restTemplate.getForObject(
                endpointExchangeRates, CurrenciesNBP[].class);
    }
}
