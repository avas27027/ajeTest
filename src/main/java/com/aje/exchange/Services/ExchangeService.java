package com.aje.exchange.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.beans.ExchangeRatesApi;
import com.beans.Service0101;

@Service
public class ExchangeService {


    public Service0101 exchangeValue(String key) {
        String uri = "http://api.exchangeratesapi.io/v1/latest?access_key="+key+"&symbols=PEN,USD,EUR";
        RestTemplate restTemplate = new RestTemplate();
        ExchangeRatesApi result = restTemplate.getForObject(uri, ExchangeRatesApi.class);

        Service0101 response = new Service0101();
        response.PEN = result.rates.PEN;
        response.EUR = result.rates.EUR;
        response.USD = result.rates.USD;
        return response;
    }
}
