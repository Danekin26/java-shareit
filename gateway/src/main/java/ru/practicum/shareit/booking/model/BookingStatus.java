package ru.practicum.shareit.booking.model;

/*
    Список статусов бронирования
 */
public enum BookingStatus {
    WAITING, // ожидает подтверждения
    APPROVED, // одобрено владельцем
    REJECTED, // отклонено владельцем
    CANCELED // отменено создателем

}
