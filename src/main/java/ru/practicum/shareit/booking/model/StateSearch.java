package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.InvalidDataEnteredException;

/*
    Список статусов для поиска бронирования
 */
public enum StateSearch {
    ALL, // все
    CURRENT, // доступные
    PAST, // завершенные
    FUTURE, // будущие
    WAITING, // ожидающие подтверждения
    REJECTED;// отклоненные

    public static StateSearch getEnumValue(String state) {
        try {
            return StateSearch.valueOf(state.toUpperCase());
        } catch (Exception e) {
            throw new InvalidDataEnteredException(state + " статус не существует");
        }
    }
}
