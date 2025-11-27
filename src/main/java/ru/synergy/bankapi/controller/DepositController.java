package ru.synergy.bankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.synergy.bankapi.service.AccountService;

import java.math.BigDecimal;

@Controller
public class DepositController {
    private final AccountService accountService;

    public DepositController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("amount") BigDecimal amount) {
        accountService.depositToAccount(amount);
        return "redirect:/account";
    }
}
