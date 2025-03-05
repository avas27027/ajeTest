package com.aje.exchange;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.beans.Service0101;
import com.google.gson.Gson;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @RequestMapping("/exchangeValue")
    public @ResponseBody String exchageValue() {
        ExchangeService service = new ExchangeService();
        Service0101 res = service.exchangeValue();

        ModelMap map = new ModelMap();
        map.put("success", true);
        map.put("data", res);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    @PostMapping("/exchangeOp")
    public String processObject(@RequestBody InputObject input) {
        Gson json = new Gson();
        ExchangeService service = new ExchangeService();
        Service0101 exchangeValue = service.exchangeValue();
        ResponseObject responseObject = new ResponseObject();
        ModelMap map = new ModelMap();
        double nuc = 0.0;
        double finalAmount = 0.0;

        try {
            if (input.transaction.equals("sell")) {
                responseObject.currency = input.endCurrency;
                responseObject.transaction = "sell";
            } else if (input.transaction.equals("buy")) {
                String temp = input.originCurrency;
                input.originCurrency = input.endCurrency;
                input.endCurrency = temp;

                responseObject.currency = input.originCurrency;
                responseObject.transaction = "buy";
            } else {
                map.put("success", false);
                map.put("error", "no transaction");
                return json.toJson(map);
            }

            switch (input.originCurrency) {
                case "PEN":
                    nuc = input.amount / exchangeValue.PEN;
                    break;
                case "USD":
                    nuc = input.amount;
                    break;
                default:
                    map.put("success", false);
                    map.put("error", "no originCurrency");
                    return json.toJson(map);
            }

            switch (input.endCurrency) {
                case "PEN":
                    finalAmount = nuc * exchangeValue.PEN;
                    break;
                case "USD":
                    finalAmount = nuc;
                    break;
                default:
                    map.put("success", false);
                    map.put("error", "no endCurrency");
                    return json.toJson(map);
            }
            responseObject.amount = finalAmount;
            map.put("success", true);
            map.put("data", responseObject);

        } catch (Exception e) {
            System.out.println("Message: " + (e.getMessage() == null ? "No Message" : e.getMessage()));
        }
        return json.toJson(map);
    }

    static class InputObject {
        public String transaction = "";
        public double amount = 0.0;
        public String originCurrency = "";
        public String endCurrency = "";
    }

    public class ResponseObject {
        public double amount = 0.0;
        public String currency = "";
        public String transaction = "";
    }
}
