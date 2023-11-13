package ru.practicum.shareit.booking.model;

/*
    Список статусов для поиска бронирования
 */
public enum StateSearch {
    ALL, // все
    CURRENT, // доступные
    PAST, // завершенные
    FUTURE, // будущие
    WAITING, // ожидающие подтверждения
    REJECTED // отклоненные
}
