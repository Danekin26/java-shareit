package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

/*
    Сокращенный объект брониварония
 */
@Data
@Builder
public class BookingShort {
    private Long id;
    private Long bookerId;
}
