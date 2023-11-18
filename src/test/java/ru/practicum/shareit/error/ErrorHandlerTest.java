package ru.practicum.shareit.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BookingUnavailableItemException;
import ru.practicum.shareit.exception.DuplicationOfExistingDataException;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.exception.NonExistingStatusException;
import ru.practicum.shareit.exception.model.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @Mock
    private LackOfRequestedDataException lackOfRequestedDataException;

    @Mock
    private DuplicationOfExistingDataException duplicationOfExistingDataException;

    @Mock
    private InvalidDataEnteredException invalidDataEnteredException;

    @Mock
    private BookingUnavailableItemException bookingUnavailableItemException;

    @Mock
    private NonExistingStatusException nonExistingStatusException;

    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void handleLackOfRequestedDataExceptionTest() {
        when(lackOfRequestedDataException.getMessage()).thenReturn("Not found");
        ErrorResponse response = errorHandler.authorizationException(lackOfRequestedDataException);
        assertEquals("Not found", response.getError());
    }

    @Test
    void handleDuplicationOfExistingDataExceptionTest() {
        when(duplicationOfExistingDataException.getMessage()).thenReturn("Duplicate data");
        ErrorResponse response = errorHandler.authorizationException(duplicationOfExistingDataException);
        assertEquals("Duplicate data", response.getError());
    }

    @Test
    void handleInvalidDataEnteredExceptionTest() {
        when(invalidDataEnteredException.getMessage()).thenReturn("Invalid data");
        ErrorResponse response = errorHandler.authorizationException(invalidDataEnteredException);
        assertEquals("Invalid data", response.getError());
    }

    @Test
    void handleBookingUnavailableItemExceptionTest() {
        when(bookingUnavailableItemException.getMessage()).thenReturn("Booking unavailable");
        ErrorResponse response = errorHandler.authorizationException(bookingUnavailableItemException);
        assertEquals("Booking unavailable", response.getError());
    }

    @Test
    void handleNonExistingStatusExceptionTest() {
        when(nonExistingStatusException.getMessage()).thenReturn("Non-existing status");
        ErrorResponse response = errorHandler.authorizationException(nonExistingStatusException);
        assertEquals("Non-existing status", response.getError());
    }
}
