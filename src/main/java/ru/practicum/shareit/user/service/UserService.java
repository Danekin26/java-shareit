package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    @Qualifier("userInMemmory")
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public User updateUser(int id, User user) {
        return userStorage.updateUser(id, user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUser();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
}
