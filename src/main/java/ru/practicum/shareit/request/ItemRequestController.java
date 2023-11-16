package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
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
                                                                @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                                @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
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
