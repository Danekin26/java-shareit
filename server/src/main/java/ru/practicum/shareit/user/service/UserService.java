package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/*
    Интерфейс сервиса для управления сущностью пользователь
 */
public interface UserService {
    /*
        Создать пользователя
     */
    UserDto createUser(User user);

    /*
        Удалить пользователя
     */
    void deleteUser(Long id);

    /*
        Обновить пользователя
     */
    UserDto updateUser(Long id, User user);

    /*
        Получить всех пользователей
     */
    List<UserDto> getAllUsers();

    /*
        Получить пользователя по id
     */
    UserDto getUserById(Long id);
}
