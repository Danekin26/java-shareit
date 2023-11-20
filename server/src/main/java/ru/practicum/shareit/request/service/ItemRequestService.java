package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/*
    Интерфейс сервиса для управления сущностью запроса
 */
public interface ItemRequestService {
    /*
        Создать запрос
     */
    ItemRequestDtoOut createRequest(Long userId, ItemRequest request);

    /*
        Получить запросы пользователя
     */
    List<ItemRequestDtoOut> getRequestsUser(Long userId);

    /*
        Получить список запросов других пользователей
     */
    List<ItemRequestDtoOut> getRequestsCreatedOtherUsers(Long userId, Integer from, Integer size);

    /*
        Получить определенный запрос
     */
    ItemRequestDtoOut getRequestDtoOut(Long requestId, Long userId);
}
