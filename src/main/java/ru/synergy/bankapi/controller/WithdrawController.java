package ru.synergy.bankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.synergy.bankapi.exception.InsufficientFundsException;
import ru.synergy.bankapi.service.AccountService;

import java.math.BigDecimal;

@Controller
public class WithdrawController {
    private final AccountService accountService;

    public WithdrawController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("amount") BigDecimal amount,
                           RedirectAttributes redirectAttributes) {
        try {
            accountService.withdrawFromAccount(amount);
            return "redirect:/account";
        } catch (InsufficientFundsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/account";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            return "redirect:/account";
        }
    }
}
