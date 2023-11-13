package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.item.dto.item.ItemDtoMapper.itemToDto;
import static ru.practicum.shareit.user.dto.UserDtoMapper.userToDto;

/*
    Маппер объектов брониварония
 */
public class BookingDtopMapper {

    public static Booking dtoToBooking(BookingDtoIn bookingDtoIn, User user, Item item) {
        return Booking.builder()
                .id(bookingDtoIn.getIdBooking())
                .start(bookingDtoIn.getStart())
                .end(bookingDtoIn.getEnd())
                .item(item)
                .booker(user)
                .status(bookingDtoIn.getStatus())
                .build();
    }

    public static BookingDtoOut bookingToBookingDtoOut(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .booker(userToDto(booking.getBooker()))
                .item(itemToDto(booking.getItem()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingDtoOut> allBookingToAllBookingDtoOut(List<Booking> allBooking) {
        List<BookingDtoOut> allBookingDto = new ArrayList<>();
        for (Booking booking : allBooking) {
            allBookingDto.add(bookingToBookingDtoOut(booking));
        }
        return allBookingDto;
    }

    public static BookingShort bookingToBookingShort(Booking booking) {
        if (booking == null) return null;
        return BookingShort.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
