package ru.practicum.shareit.booking.model;

import lombok.Getter;

import java.util.Arrays;

public enum BookingStatus {
    WAITING(1), // новое бронирование
    APPROVED(2), // бронирование подтверждено владельцем
    REJECTED(3), // бронирование отклонено владельцем
    CANCELED(4); // бронирование отменено создателем
    @Getter
    private int id;

    BookingStatus(int id) {
        this.id = id;
    }

    public BookingStatus getBookingStatusById(int id) {
        return Arrays.stream(BookingStatus.values())
                .filter(bookingStatus -> bookingStatus.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
