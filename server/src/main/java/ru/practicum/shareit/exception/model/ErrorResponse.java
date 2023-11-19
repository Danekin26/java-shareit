package ru.practicum.shareit.exception.model;

import lombok.Data;

/*
    Сущность исключения
*/
@Data
public class ErrorResponse {
    private final String error;
}
