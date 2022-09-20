package com.tgrajkowski.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.tgrajkowski.model.nbp.CurrenciesNBP;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;


class RetrieveDataFromNBPApiTest {
    private WireMockServer wireMockServer;

    @BeforeEach
    public void beforeEach() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort().dynamicPort());
        wireMockServer.start();
    }

    @Test
    public void shouldRetrieveDataFromNbp() {
        // given
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        String endpointExchangeRates = "/api/exchangerates/tables/a/2022-09-23/?format=json";

        RestTemplate restTemplate = restTemplateBuilder.rootUri("http://localhost:" + wireMockServer.port()).build();
        RetrieveDataFromNBPApi retrieveDataFromNBPApi = new RetrieveDataFromNBPApi(restTemplate);
        retrieveDataFromNBPApi.setEndpointExchangeRates(endpointExchangeRates);

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(endpointExchangeRates))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[\n" +
                                "    {\n" +
                                "        \"table\": \"A\",\n" +
                                "        \"no\": \"185/A/NBP/2022\",\n" +
                                "        \"effectiveDate\": \"2022-09-23\",\n" +
                                "        \"rates\": [\n" +
                                "            {\n" +
                                "                \"currency\": \"dolar amerykański\",\n" +
                                "                \"code\": \"USD\",\n" +
                                "                \"mid\": 4.8796\n" +
                                "            }\n" +
                                "        ]\n" +
                                "    }\n" +
                                "]")));

        // when
        CurrenciesNBP[] currenciesNBPArray = retrieveDataFromNBPApi.getData();

        // then
        Assertions.assertThat(currenciesNBPArray.length).isEqualTo(1);
        Assertions.assertThat(currenciesNBPArray[0].getTable()).isEqualTo("A");
        Assertions.assertThat(currenciesNBPArray[0].getNo()).isEqualTo("185/A/NBP/2022");
        Assertions.assertThat(currenciesNBPArray[0].getEffectiveDate()).isEqualTo(LocalDate.of(2022, 9, 23));
        Assertions.assertThat(currenciesNBPArray[0].getRateDtoList().size()).isEqualTo(1);
        Assertions.assertThat(currenciesNBPArray[0].getRateDtoList().get(0).getCurrency()).isEqualTo("dolar amerykański");
        Assertions.assertThat(currenciesNBPArray[0].getRateDtoList().get(0).getCode()).isEqualTo("USD");
        Assertions.assertThat(currenciesNBPArray[0].getRateDtoList().get(0).getMid()).isEqualTo("4.8796");
    }
}