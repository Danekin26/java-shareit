package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

/*
    Маппер пользователей
*/
public class UserDtoMapper {
    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User dtoToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static List<UserDto> allUserToAllUserDto(List<User> allUser) {
        List<UserDto> allUserDto = new ArrayList<>();
        for (User user : allUser) {
            allUserDto.add(userToDto(user));
        }
        return allUserDto;
    }
}
