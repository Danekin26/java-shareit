package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/*
    Контроллера для управления запросами бронирования
*/
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    /*
        Создать бронирование
    */
    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid BookingDtoIn booking) {
        log.info("Выполняется POST-запрос. Создание нового бронирования.");
        return bookingClient.createBooking(userId, booking);
    }

    /*
        Управление статусом бронирования
    */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingConfirmation(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId,
                                                      @RequestParam Boolean approved) {
        log.info("Выполняется PATCH-запрос. Управление статусом подтверждения бронирования.");
        return bookingClient.bookingConfirmation(userId, bookingId, approved);
    }

    /*
        Получить бронирование по id бронирования
    */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос. Получить бронирование.");
        return bookingClient.getBooking(userId, bookingId);
    }

    /*
         Получить бронирование по id пользователя
     */
    @GetMapping()
    public ResponseEntity<Object> getUserBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Выполняется GET-запрос. Получить бронирования пользователя.");
        return bookingClient.getBookings(userId, state, from, size);
    }

    /*
        Получить бронирование по id владельца
    */
    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Выполняется GET-запрос. Получить бронирования владельца.");
        return bookingClient.getBookingsOwner(userId, state, from, size);
    }

}
