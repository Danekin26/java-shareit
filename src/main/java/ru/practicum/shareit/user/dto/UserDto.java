package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDto {
    public static User toUserDto(User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
