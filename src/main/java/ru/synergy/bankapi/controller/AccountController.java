package ru.synergy.bankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.synergy.bankapi.dto.ChangePasswordRequest;
import ru.synergy.bankapi.entity.AccountEntity;
import ru.synergy.bankapi.entity.TransactionEntity;
import ru.synergy.bankapi.entity.UserEntity;
import ru.synergy.bankapi.service.AccountService;
import ru.synergy.bankapi.service.UserService;

import java.util.List;

@Controller
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService,
                             UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute ChangePasswordRequest request, Model model) {
        UserEntity currentUser = accountService.getCurrentUser();
        String login = currentUser.getLogin();

        boolean success = userService.changePassword(login, request.getOldPassword(), request.getNewPassword());

        if (success) {
            model.addAttribute("message", "Пароль успешно изменён.");
        } else {
            model.addAttribute("error", "Неверный старый пароль или ошибка при смене.");
        }

        return "change-password";
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
