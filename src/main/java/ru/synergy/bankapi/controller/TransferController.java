package ru.synergy.bankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.synergy.bankapi.exception.InsufficientFundsException;
import ru.synergy.bankapi.service.AccountService;

import java.math.BigDecimal;

@Controller
public class TransferController {
    private final AccountService accountService;

    public TransferController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/transfer")
    public String transfer(
            @RequestParam("recipientLogin") String recipientLogin,
            @RequestParam("amount") BigDecimal amount,
            RedirectAttributes redirectAttributes) {
        try {
            accountService.transferToUser(recipientLogin, amount);
            return "redirect:/account";
        } catch (InsufficientFundsException e) {
            redirectAttributes.addFlashAttribute("message", "Insufficient funds");
            return "redirect:/account";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            return "redirect:/account";
        }
    }
}
