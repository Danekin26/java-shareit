package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingUnavailableItemException;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.exception.NonExistingStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.dto.BookingDtopMapper.allBookingToAllBookingDtoOut;
import static ru.practicum.shareit.booking.dto.BookingDtopMapper.bookingToBookingDtoOut;
import static ru.practicum.shareit.booking.dto.BookingDtopMapper.dtoToBooking;
import static ru.practicum.shareit.user.dto.UserDtoMapper.userToDto;

/*
    Сервис для управления сущностью бронирования
 */
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoOut createBooking(BookingDtoIn bookingDtoIn, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Пользователя с id %d не существует", userId)));
        Item item = itemRepository.findById(bookingDtoIn.getItemId()).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Предмета с id %d не существует", bookingDtoIn.getItemId()))
        );

        if (item.getOwner() != null) {
            if (item.getOwner().getId().equals(userId)) {
                throw new LackOfRequestedDataException("Ошика при создании запроса");
            }
        }

        if ((bookingDtoIn.getStart().isAfter(bookingDtoIn.getEnd())) || (bookingDtoIn.getEnd().isBefore(LocalDateTime.now()))
                || (bookingDtoIn.getStart().equals(bookingDtoIn.getEnd()))) {
            throw new InvalidDataEnteredException("Некорректно введена дата окончаия и/или начала бронирования");
        }
        Booking booking = dtoToBooking(bookingDtoIn, user, item);

        if (!item.getAvailable()) {
            throw new BookingUnavailableItemException(String.format("Предмет с id %d не доступен для бронирования",
                    item.getId()));
        }
        bookingRepository.save(booking);
        return bookingToBookingDtoOut(booking);
    }

    @Override
    @Transactional
    public BookingDtoOut bookingConfirmation(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Запроса с id %d не существует", bookingId))
        );
        if (booking.getStatus() != BookingStatus.WAITING)
            throw new InvalidDataEnteredException("Бронирование недоступно");
        UserDto owner = userToDto(userRepository.findById(booking.getItem().getOwner().getId()).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Пользователя с id %d не существует", userId))));
        if (!owner.getId().equals(userId)) {
            throw new LackOfRequestedDataException(
                    String.format("Пользователь с id %d не является владельцем предмета с id %d", userId, booking.getItem().getId())
            );
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingToBookingDtoOut(booking);
    }

    @Override
    @Transactional
    public BookingDtoOut getBooking(Long idBooking, Long userId) {
        Booking booking = bookingRepository.findById(idBooking).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Бронирования с id %d не существует", idBooking))
        );
        UserDto bookier = userToDto(userRepository.findById(booking.getBooker().getId()).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Пользователя с id %d не существует", userId))));
        UserDto owner = userToDto(userRepository.findById(booking.getItem().getOwner().getId()).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Пользователя с id %d не существует", userId))));
        if (!(bookier.getId().equals(userId) || owner.getId().equals(userId))) {
            throw new LackOfRequestedDataException(String.format("Пользователь с id = %d не может получить доступ к бронированию", userId));
        }
        return bookingToBookingDtoOut(booking);
    }

    @Override
    @Transactional
    public List<BookingDtoOut> getUserBookings(String stateSearch, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Пользователя с id %d не существует", userId))
        );
        List<BookingDtoOut> bookingDto;
        LocalDateTime timeNow = LocalDateTime.now();

        switch (stateSearch) {
            case "ALL":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findAllBookingsByUserIdUser(userId));
                break;
            case "PAST":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findUserBookingsByPast(userId));
                break;
            case "FUTURE":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findUserBookingsByFuture(userId, timeNow));
                break;
            case "CURRENT":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findUserBookingsByCurrent(userId, Sort.by(Sort.Direction.DESC, "start")));
                break;
            case "WAITING":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findUserBookingsByWatting(userId));
                break;
            case "REJECTED":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findUserBookingsByRejected(userId));
                break;
            default:
                throw new InvalidDataEnteredException(String.format("Unknown state: %s", stateSearch));
        }
        return bookingDto;
    }

    @Override
    @Transactional
    public List<BookingDtoOut> getOwnerBookings(String stateSearch, Long ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new LackOfRequestedDataException(
                String.format("Пользователя с id %d не существует", ownerId))
        );
        List<BookingDtoOut> bookingDto;
        LocalDateTime timeNow = LocalDateTime.now();

        switch (stateSearch) {
            case "ALL":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findAllBookingsByOwnerIdOwner(ownerId));
                break;
            case "PAST":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findOwnerBookingsByPast(ownerId));
                break;
            case "FUTURE":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findOwnerBookingsByFuture(ownerId, timeNow));
                break;
            case "CURRENT":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findOwnerBookingsByCurrent(ownerId, Sort.by(Sort.Direction.DESC, "start")));
                break;
            case "WAITING":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findOwnerBookingsByWatting(ownerId));
                break;
            case "REJECTED":
                bookingDto = allBookingToAllBookingDtoOut(bookingRepository.findOwnerBookingsByRejected(ownerId));
                break;
            default:
                throw new NonExistingStatusException(String.format("Unknown state: %s", stateSearch));
        }
        return bookingDto;
    }
}
