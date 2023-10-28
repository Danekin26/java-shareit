package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicationOfExistingDataException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserDto.toUserDto;

@Component("userInMemmory")
public class UserStorageImpl implements UserStorage {
    private Map<Integer, User> idUser = new HashMap<>();
    private int nextId = 1;

    @Override
    public User createUser(User user) {
        checkDuplicateUser(user);
        user.setId(nextId);
        nextId++;
        idUser.put(user.getId(), user);
        return toUserDto(user);
    }

    @Override
    public List<User> getAllUser() {
        return idUser.values().stream()
                .map(UserDto::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(int id) {
        idUser.remove(id);
    }

    @Override
    public User updateUser(int id, User user) {
        if (idUser.containsKey(id)) {
            user.setId(id);
            createUpdateUser(user);
            idUser.put(id, user);
            return toUserDto(user);
        }
        throw new LackOfRequestedDataException(String.format("Пользователь с id = %s не существует", id));
    }

    @Override
    public User getUserById(int id) {
        return idUser.values().stream().filter(searchUser -> searchUser.getId() == id)
                .findFirst()
                .map(UserDto::toUserDto)
                .orElseThrow(() -> new LackOfRequestedDataException(String.format("Пользователь с id = %s не существует", id)));
    }

    /*
        Проверка на дубликацию пользователей
     */
    private void checkDuplicateUser(User user) {
        boolean hasDuplicateEmail = idUser.values().stream()
                .anyMatch(userInMap -> ((user.getEmail().equals(userInMap.getEmail())) && (user.getId() != userInMap.getId())));


        if (hasDuplicateEmail) {
            throw new DuplicationOfExistingDataException(String.format("Пользователь с email %s уже существует",
                    user.getEmail()));
        }
    }

    /*
        Создание обновленного пользователя
     */
    private void createUpdateUser(User updateUser) {
        User oldUser = idUser.get(updateUser.getId());
        if (updateUser.getName() == null) updateUser.setName(oldUser.getName());
        if (updateUser.getEmail() == null) updateUser.setEmail(oldUser.getEmail());
        checkDuplicateUser(updateUser);
    }
}
