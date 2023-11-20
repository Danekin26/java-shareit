package ru.practicum.shareit.exception;

/*
    Исключение при недоступном предмете при бронировании
 */
public class BookingUnavailableItemException extends RuntimeException {
    public BookingUnavailableItemException(String mes) {
        super(mes);
    }
}
