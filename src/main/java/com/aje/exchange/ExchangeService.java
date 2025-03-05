package com.aje.exchange;

import com.beans.Service0101;

public class ExchangeService {
    public Service0101 exchangeValue() {
        Service0101 response = new Service0101();
        response.PEN = 3.69;
        response.USD = 1;
        return response;
    }
}
