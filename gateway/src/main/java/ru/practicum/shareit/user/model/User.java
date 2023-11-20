package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
    Объект пользователей в базе данных
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id; // id пользователя
    @NotNull
    @NotBlank
    private String name; // имя пользователя
    @NotNull
    @NotBlank
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email; // email пользователя
}
