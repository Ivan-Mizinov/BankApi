package ru.synergy.bankapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    private String oldPassword;
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    private String newPassword;
}
