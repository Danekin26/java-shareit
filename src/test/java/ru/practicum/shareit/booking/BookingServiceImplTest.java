package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.dto.BookingDtopMapper.bookingToBookingDtoOut;
import static ru.practicum.shareit.item.dto.item.ItemDtoMapper.itemToDto;
import static ru.practicum.shareit.user.dto.UserDtoMapper.userToDto;

@SpringBootTest
class BookingServiceImplTest {
    @Autowired
    private BookingService bookingService;


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private BookingRepository bookingRepository;


    private User userOne;
    private User userTwo;
    private Item itemOne;

    private Booking bookingOne;

    private BookingDtoIn bookingDtoIn;
    private BookingDtoOut bookingDtoOut;

    @BeforeEach
    void beforeEach() {
        userOne = User.builder()
                .id(1L)
                .name("one")
                .email("one@mail.ru")
                .build();

        userTwo = User.builder()
                .id(2L)
                .name("two")
                .email("two@mail.ru")
                .build();

        itemOne = Item.builder()
                .id(1L)
                .name("item")
                .description("one item")
                .available(true)
                .owner(userTwo)
                .build();

        bookingOne = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 8, 20, 12, 20))
                .end(LocalDateTime.of(2025, 8, 25, 10, 0))
                .item(itemOne)
                .booker(userOne)
                .status(BookingStatus.WAITING)
                .build();

        bookingDtoIn = BookingDtoIn.builder()
                .idBooking(1L)
                .itemId(1L)
                .start(LocalDateTime.of(2024, 8, 20, 12, 20))
                .end(LocalDateTime.of(2025, 8, 25, 10, 0))
                .status(BookingStatus.WAITING)
                .build();

        bookingDtoOut = BookingDtoOut.builder()
                .id(1L)
                .item(itemToDto(itemOne))
                .start(LocalDateTime.of(2024, 8, 20, 12, 20))
                .end(LocalDateTime.of(2025, 8, 25, 10, 0))
                .status(BookingStatus.WAITING)
                .booker(userToDto(userOne))
                .build();
    }

    @Test
    void createBookingTest() {
        when(userRepository.findById(userOne.getId())).thenReturn(Optional.of(userOne));
        when(itemRepository.findById(itemOne.getId())).thenReturn(Optional.of(itemOne));

        BookingDtoOut result = bookingService.createBooking(bookingDtoIn, 1L);

        verify(userRepository, times(1)).findById(userOne.getId());
        verify(itemRepository, times(1)).findById(bookingDtoIn.getItemId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertNotNull(result);
    }

    @Test
    void bookingConfirmationTest() {
        when(userRepository.findById(userOne.getId())).thenReturn(Optional.of(userOne));
        when(userRepository.findById(userTwo.getId())).thenReturn(Optional.of(userTwo));
        when(itemRepository.findById(itemOne.getId())).thenReturn(Optional.of(itemOne));
        when(bookingRepository.findById(bookingOne.getId())).thenReturn(Optional.of(bookingOne));

        BookingDtoOut savedBooking = bookingService.createBooking(bookingDtoIn, 1L);
        BookingDtoOut result = bookingService.bookingConfirmation(userTwo.getId(), bookingOne.getId(), true);

        verify(userRepository, times(1)).findById(userTwo.getId());
        verify(itemRepository, times(1)).findById(bookingOne.getItem().getId());
        verify(bookingRepository, times(2)).save(any(Booking.class));
        assertEquals(result.getStatus(), BookingStatus.APPROVED);
        assertNotNull(result);
    }

    @Test
    void getBookingTest() {
        when(userRepository.findById(userOne.getId())).thenReturn(Optional.of(userOne));
        when(userRepository.findById(userTwo.getId())).thenReturn(Optional.of(userTwo));
        when(itemRepository.findById(itemOne.getId())).thenReturn(Optional.of(itemOne));
        when(bookingRepository.findById(bookingOne.getId())).thenReturn(Optional.of(bookingOne));

        BookingDtoOut result = bookingService.getBooking(bookingOne.getId(), userOne.getId());

        verify(userRepository, times(2)).findById(anyLong());
        verify(bookingRepository, times(1)).findById(bookingOne.getId());
        assertEquals(result, bookingDtoOut);

    }

    @Test
    void getUserBookingsTest() {
        when(userRepository.findById(userOne.getId())).thenReturn(Optional.of(userOne));
        when(userRepository.findById(userTwo.getId())).thenReturn(Optional.of(userTwo));
        when(itemRepository.findById(itemOne.getId())).thenReturn(Optional.of(itemOne));
        when(bookingRepository.findById(bookingOne.getId())).thenReturn(Optional.of(bookingOne));
        when(bookingRepository.findAllBookingsByUserIdUser(anyLong(), any(PageRequest.class))).thenReturn(List.of(bookingOne));

        List<BookingDtoOut> result = bookingService.getUserBookings("ALL", userOne.getId(), 5, 10);
        List<BookingDtoOut> allBooking = new ArrayList<>();
        allBooking.add(bookingToBookingDtoOut(bookingOne));

        assertEquals(result, allBooking);
    }

    @Test
    void getOwnerBookingsTest() {
        when(userRepository.findById(userOne.getId())).thenReturn(Optional.of(userOne));
        when(userRepository.findById(userTwo.getId())).thenReturn(Optional.of(userTwo));
        when(itemRepository.findById(itemOne.getId())).thenReturn(Optional.of(itemOne));
        when(bookingRepository.findById(bookingOne.getId())).thenReturn(Optional.of(bookingOne));
        when(bookingRepository.findAllBookingsByOwnerIdOwner(anyLong(), any(PageRequest.class))).thenReturn(List.of(bookingOne));

        List<BookingDtoOut> result = bookingService.getOwnerBookings("ALL", userTwo.getId(), 5, 10);
        List<BookingDtoOut> allBooking = new ArrayList<>();
        allBooking.add(bookingToBookingDtoOut(bookingOne));

        assertEquals(result, allBooking);
    }

    @Test
    void createBookingWhenInvalidUserIdThenThrowExceptionTest() {
        BookingDtoIn bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setUserId(1L);

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, null));
    }

    @Test
    void createBookingWhenInvalidItemIdThenThrowExceptionTest() {
        BookingDtoIn bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setUserId(1L);
        bookingDtoIn.setItemId(1L);

        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, 1L));
    }

    @Test
    void createBookingWhenSameUserAsOwnerThenThrowExceptionTest() {
        BookingDtoIn bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setUserId(1L);
        bookingDtoIn.setItemId(2L);

        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setOwner(user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, 1L));
    }

    @Test
    void createBookingWhenInvalidDateThenThrowExceptionTest() {
        BookingDtoIn bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setUserId(1L);
        bookingDtoIn.setItemId(2L);
        bookingDtoIn.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoIn.setEnd(LocalDateTime.now());

        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setOwner(user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, 1L));
    }

    @Test
    void createBookingWhenItemNotAvailableThenThrowExceptionTest() {
        BookingDtoIn bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setUserId(1L);
        bookingDtoIn.setItemId(2L);
        bookingDtoIn.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoIn.setEnd(LocalDateTime.now().plusDays(2));

        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setOwner(user);
        item.setAvailable(false);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, 1L));
    }

    @Test
    void createBookingWhenOwnerIsBookerThenThrowExceptionTest() {
        BookingDtoIn bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setUserId(1L);
        bookingDtoIn.setItemId(2L);
        bookingDtoIn.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoIn.setEnd(LocalDateTime.now().plusDays(2));

        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setOwner(user);
        item.setAvailable(true);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, 1L));
    }

    @Test
    void bookingConfirmationWhenBookingIdNotFoundThenThrowExceptionTest() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.bookingConfirmation(userId, bookingId, approved));
    }

    @Test
    void bookingConfirmationWhenBookingNotInWaitingStatusThenThrowExceptionTest() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(InvalidDataEnteredException.class,
                () -> bookingService.bookingConfirmation(userId, bookingId, approved));
    }

    @Test
    void bookingConfirmationWhenUserNotOwnerThenThrowExceptionTest() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);

        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.bookingConfirmation(userId, bookingId, approved));
    }

    @Test
    void getBookingWhenBookingNotFoundThenThrowExceptionTest() {
        Long bookingId = 1L;
        Long userId = 2L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.getBooking(bookingId, userId));
    }

    @Test
    void getBookingWhenBookerNotFoundThenThrowExceptionTest() {
        Long bookingId = 1L;
        Long userId = 2L;

        Booking booking = new Booking();
        booking.setBooker(new User());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(booking.getBooker().getId())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.getBooking(bookingId, userId));
    }

    @Test
    void getBookingWhenOwnerNotFoundThenThrowExceptionTest() {
        Long bookingId = 1L;
        Long userId = 2L;

        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(booking.getBooker().getId())).thenReturn(Optional.of(new User()));
        when(userRepository.findById(booking.getItem().getOwner().getId())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.getBooking(bookingId, userId));
    }

    @Test
    void getBookingWhenAllDataValidThenReturnBookingDtoOutTest() {
        Long bookingId = 1L;
        Long userId = 2L;

        Booking booking = new Booking();
        User booker = new User();
        booker.setId(userId);
        booking.setBooker(booker);
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(userId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(booking.getBooker().getId())).thenReturn(Optional.of(booker));
        when(userRepository.findById(booking.getItem().getOwner().getId())).thenReturn(Optional.of(booker));

        BookingDtoOut result = bookingService.getBooking(bookingId, userId);

        assertEquals(bookingToBookingDtoOut(booking), result);
    }

    @Test
    void bookingConfirmationInvalidStatusTest() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.of(booking));

        assertThrows(InvalidDataEnteredException.class, () -> bookingService.bookingConfirmation(userId, bookingId, approved));
        Mockito.verify(bookingRepository, Mockito.never()).save(booking);
    }

    @Test
    public void bookingConfirmationNonExistingBookingTest() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            bookingService.bookingConfirmation(userId, bookingId, approved);
        });
    }

    @Test
    void bookingConfirmationBookingNotFoundTest() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;

        when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> bookingService.bookingConfirmation(userId, bookingId, approved));
        Mockito.verify(bookingRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getUserBookingsInvalidUserIdThrowExceptionTest() {
        Long invalidUserId = 999L;
        when(userRepository.findById(invalidUserId)).thenReturn(java.util.Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.getUserBookings("ALL", invalidUserId, 0, 10));
    }
}