package ru.practicum.shareit.exception;

/*
    Исключение при введении неверных данных
 */
public class InvalidDataEnteredException extends RuntimeException {
    public InvalidDataEnteredException(String mes) {
        super(mes);
    }
}
