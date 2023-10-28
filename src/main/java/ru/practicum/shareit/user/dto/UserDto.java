package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/*
    Сущность DTO пользователя
*/
@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    private String name;
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    @NotBlank
    private String email;
}
