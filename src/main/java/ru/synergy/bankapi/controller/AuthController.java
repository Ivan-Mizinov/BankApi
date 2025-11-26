package ru.synergy.bankapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.synergy.bankapi.dto.LoginRequest;
import ru.synergy.bankapi.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        if (userService.authenticate(request.getLogin(), request.getPassword()).isPresent()) {
            return ResponseEntity.ok("Авторизация успешна");
        } else {
            return ResponseEntity.status(401).body("Неверный логин или пароль");
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
