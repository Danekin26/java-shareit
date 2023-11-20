package ru.practicum.shareit.exception;

/*
    Исключение при несуществующем статусе
 */
public class NonExistingStatusException extends RuntimeException {
    public NonExistingStatusException(String mes) {
        super(mes);
    }
}
