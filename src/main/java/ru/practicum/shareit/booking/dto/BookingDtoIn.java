package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*
    Объект бронирования на входе
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingDtoIn {
    private Long idBooking;
    @NotNull
    private Long itemId;

    @Future
    @NotNull
    private LocalDateTime start;

    @Future
    @NotNull
    private LocalDateTime end;

    private BookingStatus status = BookingStatus.WAITING;
}
