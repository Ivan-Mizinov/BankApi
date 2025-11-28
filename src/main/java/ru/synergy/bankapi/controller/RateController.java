package ru.synergy.bankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.synergy.bankapi.dto.ExchangeRateDto;
import ru.synergy.bankapi.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RateController {

    private final ExchangeRateService rateService;

    public RateController(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/rates")
    public String showRates(Model model) {
        try {
            List<ExchangeRateDto> rates = rateService.getLatestRates();
            model.addAttribute("rates", rates);
            return "rates";
        } catch (Exception e) {
            throw new RuntimeException("Error fetching exchange rates", e);
        }
    }
}
