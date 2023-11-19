package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

/*
    Интерфейс сервиса для управления сущностью бронирования
 */
public interface BookingService {
    /*
        Создать бронирование
    */
    BookingDtoOut createBooking(BookingDtoIn bookingDtoIn, Long userId);

    /*
        Управление статусом бронирования
    */
    BookingDtoOut bookingConfirmation(Long userId, Long bookingId, Boolean approved);

    /*
        Получить бронирование по id
    */
    BookingDtoOut getBooking(Long idBooking, Long userId);

    /*
        Получить бронирование по id пользователя
    */
    List<BookingDtoOut> getUserBookings(String stateSearch, Long userId, Integer from, Integer size);

    /*
        Получить бронирование по id владельца
    */
    List<BookingDtoOut> getOwnerBookings(String stateSearch, Long ownerId, Integer from, Integer size);
}
