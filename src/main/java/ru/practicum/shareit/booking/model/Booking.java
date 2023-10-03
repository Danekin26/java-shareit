package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class Booking {
    private int id; // id бронирования
    private LocalDateTime start; // начало бронирования
    private LocalDateTime end; // конец бронирования
    private Item item; // вещь бронирования
    private User booker; // пользователь взявший в аренду
    private BookingStatus status; // статус бронирования
}
