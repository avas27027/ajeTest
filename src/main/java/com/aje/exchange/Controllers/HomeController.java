package com.aje.exchange.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin
@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }
}
