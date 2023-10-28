package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse authorizationException(final LackOfRequestedDataException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Возникла ошибки при запросе несуществующих данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse authorizationException(final DuplicationOfExistingDataException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Возникла ошибки при попытке дублирования существующих данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse authorizationException(final InvalidDataEnteredException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Возникла ошибки при некорректно введенных данных", e.getMessage());
    }
}