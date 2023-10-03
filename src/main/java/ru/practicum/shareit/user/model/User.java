package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class User {
    private int id; // id пользователя
    @NotNull
    private String name; // имя пользователя
    @NotNull
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email; // email пользователя
}
