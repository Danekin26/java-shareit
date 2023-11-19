package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/*
    Контроллер сущности запроса
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    @Autowired
    private final ItemRequestClient itemRequestClient;

    /*
        Создать запрос
     */
    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid ItemRequestDtoIn request) {
        log.info("Выполняется POST-запрос.");
        return itemRequestClient.createRequest(userId, request);
    }

    /*
        Получить список своих запросов
     */
    @GetMapping
    public ResponseEntity<Object> getRequestsUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос.");
        return itemRequestClient.getRequestsUser(userId);
    }

    /*
        Получить список запросов других пользователей
     */
    @GetMapping("/all")
    @Validated
    public ResponseEntity<Object> getRequestsCreatedOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                               @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Выполняется GET-запрос.");
        return itemRequestClient.getRequestsCreatedOtherUsers(userId, from, size);
    }

    /*
        Получить определенный запрос
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос.");
        return itemRequestClient.getRequestDtoOut(requestId, userId);
    }
}
