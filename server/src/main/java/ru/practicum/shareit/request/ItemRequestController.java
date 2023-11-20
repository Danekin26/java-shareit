package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/*
    Контроллер сущности запроса
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    /*
        Создать запрос
     */
    @PostMapping
    public ItemRequestDtoOut createRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemRequest request) {
        log.info("Выполняется POST-запрос.");
        return service.createRequest(userId, request);
    }


    /*
        Получить список своих запросов
     */
    @GetMapping
    public List<ItemRequestDtoOut> getRequestsUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос.");
        return service.getRequestsUser(userId);
    }

    /*
        Получить список запросов других пользователей
     */
    @GetMapping("/all")
    @Validated
    public List<ItemRequestDtoOut> getRequestsCreatedOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                @RequestParam(name = "from") Integer from,
                                                                @RequestParam(name = "size") Integer size) {
        log.info("Выполняется GET-запрос.");
        return service.getRequestsCreatedOtherUsers(userId, from, size);
    }

    /*
        Получить определенный запрос
     */
    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getRequest(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос.");
        return service.getRequestDtoOut(requestId, userId);
    }
}
