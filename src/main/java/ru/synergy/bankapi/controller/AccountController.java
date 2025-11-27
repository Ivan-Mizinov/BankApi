package ru.synergy.bankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.synergy.bankapi.entity.AccountEntity;
import ru.synergy.bankapi.entity.TransactionEntity;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.service.AccountService;

import java.util.List;

@Controller
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account")
    public String account(Model model) {
        UserEntity user = accountService.getCurrentUser();
        AccountEntity account = accountService.getUserAccount();
        List<TransactionEntity> transactions = accountService.getLast10Transactions();

        model.addAttribute("user", user);
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        return "account";
    }
}
