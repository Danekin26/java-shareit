package ru.practicum.shareit.exception;

/*
    Исключение при повторном создании уже существующих данных
 */
public class DuplicationOfExistingDataException extends RuntimeException {
    public DuplicationOfExistingDataException(String mes) {
        super(mes);
    }
}
