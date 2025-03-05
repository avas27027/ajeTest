package com.beans;



public class ExchangeRatesApi {
    public class RatesC {
        public double PEN = 0.0;
        public double USD = 0.0;
        public double EUR = 0.0;
    }

    public String success = "";
    public String timestamp = "";
    public String base = "";
    public String date = "";
    public RatesC rates = new RatesC();
}

