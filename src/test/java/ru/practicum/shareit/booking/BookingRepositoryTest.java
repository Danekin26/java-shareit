package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findAllBookingsByUserIdUserTest() {
        User user = createUser("John Doe", "john@example.com");
        Item item = createItem("Laptop", "Powerful laptop for rent", true, user);
        Booking booking = createBooking(user, item, BookingStatus.APPROVED);

        List<Booking> bookings = bookingRepository.findAllBookingsByUserIdUser(user.getId(), PageRequest.of(0, 10));

        assertThat(bookings).containsExactly(booking);
    }

    @Test
    void findUserBookingsByPastTest() {
        User user = createUser("John Doe", "john@example.com");
        Item item = createItem("Laptop", "Powerful laptop for rent", true, user);
        Booking pastBooking = createBooking(user, item, BookingStatus.APPROVED,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(2));
        Booking futureBooking = createBooking(user, item, BookingStatus.APPROVED,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(5));

        List<Booking> pastBookings = bookingRepository.findUserBookingsByPast(user.getId(), PageRequest.of(0, 10));

        assertThat(pastBookings).containsExactly(pastBooking);
    }


    @Test
    void findUserBookingsByFutureTest() {
        User user = createUser("John Doe", "john@example.com");
        Item item = createItem("Laptop", "Powerful laptop for rent", true, user);
        Booking futureBooking = createBooking(user, item, BookingStatus.APPROVED,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(5));

        List<Booking> futureBookings = bookingRepository.findUserBookingsByFuture(user.getId(),
                LocalDateTime.now(), PageRequest.of(0, 10));

        assertThat(futureBookings).containsExactly(futureBooking);
    }

    @Test
    void findUserBookingsByCurrentTest() {
        User user = createUser("John Doe", "john@example.com");
        Item item = createItem("Laptop", "Powerful laptop for rent", true, user);
        Booking currentBooking = createBooking(user, item, BookingStatus.APPROVED,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(2));

        List<Booking> currentBookings = bookingRepository.findUserBookingsByCurrent(user.getId(),
                PageRequest.of(0, 10));

        assertThat(currentBookings).containsExactly(currentBooking);
    }

    @Test
    void findUserBookingsByWattingTest() {
        User user = createUser("John Doe", "john@example.com");
        Item item = createItem("Laptop", "Powerful laptop for rent", true, user);
        Booking waitingBooking = createBooking(user, item, BookingStatus.WAITING);

        List<Booking> waitingBookings = bookingRepository.findUserBookingsByWatting(user.getId(),
                PageRequest.of(0, 10));

        assertThat(waitingBookings).containsExactly(waitingBooking);
    }

    @Test
    void findUserBookingsByRejectedTest() {
        User user = createUser("John Doe", "john@example.com");
        Item item = createItem("Laptop", "Powerful laptop for rent", true, user);
        Booking rejectedBooking = createBooking(user, item, BookingStatus.REJECTED);

        List<Booking> rejectedBookings = bookingRepository.findUserBookingsByRejected(user.getId(),
                PageRequest.of(0, 10));

        assertThat(rejectedBookings).containsExactly(rejectedBooking);
    }

    private User createUser(String name, String email) {
        return userRepository.save(User.builder().name(name).email(email).build());
    }

    private Item createItem(String name, String description, boolean available, User owner) {
        return itemRepository.save(Item.builder().name(name).description(description)
                .available(available).owner(owner).build());
    }

    private Booking createBooking(User booker, Item item, BookingStatus status) {
        return createBooking(booker, item, status, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
    }

    private Booking createBooking(User booker, Item item, BookingStatus status,
                                  LocalDateTime start, LocalDateTime end) {
        return bookingRepository.save(Booking.builder().booker(booker).item(item)
                .status(status).start(start).end(end).build());
    }
}
