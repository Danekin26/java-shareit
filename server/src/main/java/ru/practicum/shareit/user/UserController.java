package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

/*
    Контроллер для управления запросами пользователь
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl service;

    /*
        Создание пользователя
     */
    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        log.info("Выполняется POST-запрос. Создание пользователя.");
        return service.createUser(user);
    }

    /*
        Удалить пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Выполняется DELETE-запрос. Удаление пользователя.");
        service.deleteUser(id);
    }

    /*
        Обновить пользователя
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Выполняется PATCH-запрос. Обновление пользователя.");
        return service.updateUser(id, user);
    }

    /*
        Получить всех пользователей
     */
    @GetMapping
    public List<UserDto> getAllUser() {
        log.info("Выполняется GET-запрос. Получение всех пользователей.");
        return service.getAllUsers();
    }

    /*
        Получить пользователя по id
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Выполняется GET-запрос. Получение пользователя по id.");
        return service.getUserById(id);
    }
}
