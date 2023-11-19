package ru.practicum.shareit.exception;

/*
    Исключение при отсутствии запрашиваемых данных
 */
public class LackOfRequestedDataException extends RuntimeException {
    public LackOfRequestedDataException(String mes) {
        super(mes);
    }
}
