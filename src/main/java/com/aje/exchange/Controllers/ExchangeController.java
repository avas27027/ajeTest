package com.aje.exchange.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.aje.exchange.Services.ExchangeService;
import com.beans.ControllerInput0101;
import com.beans.ControllerResponse0101;
import com.beans.Service0101;
import com.google.gson.Gson;

@CrossOrigin
@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    @Value("${app.apikey}")
    private String apikey;

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @RequestMapping("/exchangeValue")
    public @ResponseBody String exchageValue() {
        Gson gson = new Gson();
        ExchangeService service = new ExchangeService();
        Service0101 serviceResponse = service.exchangeValue(apikey);
        ModelMap response = new ModelMap();
        ModelMap map = new ModelMap();

        try {
            response.put("sell PEN-USD", serviceResponse.PEN / serviceResponse.USD);
            response.put("sell PEN-EUR", serviceResponse.PEN / serviceResponse.EUR);
            response.put("buy PEN-USD", serviceResponse.USD / serviceResponse.PEN);
            response.put("buy PEN-EUR", serviceResponse.EUR / serviceResponse.PEN);
            map.put("success", true);
            map.put("data", response);

        } catch (Exception e) {
            map.put("success", false);
            System.out.println("Message: " + (e.getMessage() == null ? "No Message" : e.getMessage()));
        }

        String json = gson.toJson(map);
        return json;
    }

    @PostMapping("/exchangeOp")
    public String processObject(@RequestBody ControllerInput0101 input) {
        Gson json = new Gson();
        ExchangeService service = new ExchangeService();
        Service0101 exchangeValue = service.exchangeValue(apikey);
        ControllerResponse0101 responseObject = new ControllerResponse0101();
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
                case "EUR":
                    nuc = input.amount / exchangeValue.EUR;
                    break;
                case "USD":
                    nuc = input.amount / exchangeValue.USD;
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
                case "EUR":
                    finalAmount = nuc * exchangeValue.EUR;
                    break;
                case "USD":
                    finalAmount = nuc * exchangeValue.USD;
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

}
