package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.shareit.booking.dto.BookingDtopMapper.bookingToBookingShort;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDtoOut> json;

    @Test
    void bookingDtoTest() throws Exception {

        LocalDateTime start = LocalDateTime.of(2023, 8, 4, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 4, 12, 0);

        UserDto user = UserDto.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(2L)
                .name("screwdriver")
                .description("works well, does not ask to eat")
                .available(true)
                .build();

        BookingDtoOut bookingOutDto = BookingDtoOut.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.APPROVED.APPROVED)
                .booker(user)
                .item(itemDto)
                .build();

        JsonContent<BookingDtoOut> result = json.write(bookingOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("Anna");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("anna@yandex.ru");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("screwdriver");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
    }

    @Test
    void bookingToBookingShortTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(101L).build())
                .build();

        BookingShort bookingShort = bookingToBookingShort(booking);

        assertEquals(1L, bookingShort.getId());
        assertEquals(101L, bookingShort.getBookerId());
    }

    @Test
    void bookingToBookingShortWithNullTest() {
        Booking booking = null;
        BookingShort bookingShort = bookingToBookingShort(booking);
        assertNull(bookingShort);
    }

    @Test
    void compareToTest() {
        Booking booking1 = Booking.builder().end(LocalDateTime.of(2023, 1, 1, 12, 0)).build();
        Booking booking2 = Booking.builder().end(LocalDateTime.of(2023, 1, 2, 12, 0)).build();

        int result = booking1.compareTo(booking2);

        assertEquals(1, result);
    }
}