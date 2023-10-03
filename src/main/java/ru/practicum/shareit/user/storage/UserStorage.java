package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    /*
        Создать пользователя
     */
    User createUser(User user);

    /*
        Получить всех пользователей
    */
    List<User> getAllUser();

    /*
        Удалить пользователя
     */
    void deleteUser(int id);

    /*
        Обновить пользователя
     */
    User updateUser(int id, User user);

    /*
        Получить пользователя по id
     */
    User getUserById(int id);
}
