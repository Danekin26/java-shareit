package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

/*
    Контроллер для управления запросами пользователь
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient service;

    /*
        Создание пользователя
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        log.info("Выполняется POST-запрос. Создание пользователя.");
        return service.createUser(user);
    }

    /*
        Удалить пользователя
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Выполняется DELETE-запрос. Удаление пользователя.");
        return service.deleteUser(id);
    }

    /*
        Обновить пользователя
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Выполняется PATCH-запрос. Обновление пользователя.");
        return service.updateUser(id, user);
    }

    /*
        Получить всех пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        log.info("Выполняется GET-запрос. Получение всех пользователей.");
        return service.getAllUsers();
    }

    /*
        Получить пользователя по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Выполняется GET-запрос. Получение пользователя по id.");
        return service.getUserById(id);
    }
}
