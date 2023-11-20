package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/*
    Объект брониварония в базе данных
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Comparable<Booking> {
    private Long id; // id бронирования
    private LocalDateTime start; // начало бронирования
    private LocalDateTime end; // конец бронирования
    private Item item; // вещь бронирования
    private User booker; // пользователь взявший в аренду
    private BookingStatus status; // статус бронирования

    @Override
    public int compareTo(Booking booking) {
        return booking.getEnd().compareTo(this.getEnd());
    }
}
