package com.aje.exchange;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.beans.ExchangeRatesApi;
import com.beans.Service0101;

@Service
public class ExchangeService {
    final String uri = "http://api.exchangeratesapi.io/v1/latest?access_key=4693d045b8fc780c39469204a1740c41&symbols=PEN,USD,EUR";
    public Service0101 exchangeValue() {
        RestTemplate restTemplate = new RestTemplate();
        ExchangeRatesApi result = restTemplate.getForObject(uri, ExchangeRatesApi.class);

        Service0101 response = new Service0101();
        response.PEN = result.rates.PEN;
        response.EUR = result.rates.EUR;
        response.USD = result.rates.USD;
        return response;
    }
}
