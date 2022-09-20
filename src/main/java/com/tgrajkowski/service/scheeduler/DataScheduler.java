package com.tgrajkowski.service.scheeduler;

import com.tgrajkowski.model.currency.CurrencyMapper;
import com.tgrajkowski.model.nbp.CurrenciesNBP;
import com.tgrajkowski.model.nbp.Rate;
import com.tgrajkowski.model.nbp.RatesDao;
import com.tgrajkowski.service.RetrieveDataFromNBPApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

@Component
public class DataScheduler {
    @Autowired
    private RetrieveDataFromNBPApi retrieveDataFromNBPApi;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private RatesDao ratesDao;

    @Scheduled(cron = "0 0 0 * * ?")
    @PostConstruct
    public void importData() {
        CurrenciesNBP[] currenciesNBPS = retrieveDataFromNBPApi.getData();
        if (currenciesNBPS[0] != null) {
            List<Rate> rates = currencyMapper.mapToRateList(currenciesNBPS[0].getRateDtoList());
            rates.add(Rate.builder().currency("Polski ZŁoty").code("PLN").mid(BigDecimal.ONE).build());

            ratesDao.deleteAll();
            ratesDao.saveAll(rates);
        }
    }
}
