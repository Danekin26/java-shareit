package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/*
    Контроллера для управления запросами бронирования
*/
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    /*
        Создать бронирование
    */
    @PostMapping
    public BookingDtoOut createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody @Valid BookingDtoIn booking) {
        log.info("Выполняется POST-запрос. Создание нового бронирования.");
        return bookingServiceImpl.createBooking(booking, userId);
    }

    /*
        Управление статусом бронирования
    */
    @PatchMapping("/{bookingId}")
    public BookingDtoOut bookingConfirmation(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) {
        log.info("Выполняется PATCH-запрос. Управление статусом подтверждения бронирования.");
        return bookingServiceImpl.bookingConfirmation(userId, bookingId, approved);
    }

    /*
        Получить бронирование по id бронирования
    */
    @GetMapping("/{bookingId}")
    public BookingDtoOut getBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос. Получить бронирование.");
        return bookingServiceImpl.getBooking(bookingId, userId);
    }

    /*
        Получить бронирование по id пользователя
    */
    @GetMapping()
    public List<BookingDtoOut> getUserBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Выполняется GET-запрос. Получить бронирования пользователя.");
        return bookingServiceImpl.getUserBookings(state, userId, from, size);
    }

    /*
        Получить бронирование по id владельца
    */
    @GetMapping("/owner")
    public List<BookingDtoOut> getOwnerBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Выполняется GET-запрос. Получить бронирования владельца.");
        return bookingServiceImpl.getOwnerBookings(state, userId, from, size);
    }

}
