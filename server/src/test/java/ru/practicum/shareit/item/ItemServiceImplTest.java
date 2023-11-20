package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingUnavailableItemException;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.exception.NonExistingStatusException;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoIn;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.item.dto.item.ItemDtoMapper.itemToItemDtoIn;

@SpringBootTest
public class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    private User user;
    private Item item;
    private ItemDtoIn itemDtoIn;
    private Comment comment;
    private ItemRequest itemRequest;
    private Booking bookingOne;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("item request")
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();

        itemDtoIn = itemToItemDtoIn(item);

        comment = Comment.builder()
                .id(1L)
                .author(user)
                .created(LocalDateTime.now())
                .text("comment")
                .build();


        bookingOne = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 8, 20, 12, 20))
                .end(LocalDateTime.of(2022, 9, 20, 12, 20))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
    }

    @Test
    void createItemTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto itemDtoTest = itemService.createItem(user.getId(), itemDtoIn);

        assertEquals(itemDtoTest.getId(), itemDtoIn.getId());
        assertEquals(itemDtoTest.getDescription(), itemDtoIn.getDescription());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItemTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findAllByOwner_IdOrderByIdAsc(anyLong())).thenReturn(List.of(item));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto itemDtoTest = itemService.updateItem(item.getId(), itemDtoIn, user.getId());

        assertEquals(itemDtoTest.getId(), item.getId());
        assertEquals(itemDtoTest.getDescription(), item.getDescription());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItemByIdTest() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto itemDtoTest = itemService.getItemById(item.getId(), user.getId());

        assertEquals(itemDtoTest.getId(), item.getId());
        assertEquals(itemDtoTest.getDescription(), item.getDescription());
        assertEquals(itemDtoTest.getAvailable(), item.getAvailable());
        assertEquals(itemDtoTest.getRequestId(), item.getRequest().getId());

        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemByUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findAllByOwner_IdOrderByIdAsc(anyLong())).thenReturn(List.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto itemDtoTest = itemService.getItemByUser(user.getId()).get(0);

        assertEquals(itemDtoTest.getId(), item.getId());
        assertEquals(itemDtoTest.getDescription(), item.getDescription());
        assertEquals(itemDtoTest.getAvailable(), item.getAvailable());
        assertEquals(itemDtoTest.getRequestId(), item.getRequest().getId());

        verify(itemRepository, times(1)).findAllByOwner_IdOrderByIdAsc(anyLong());
    }

    @Test
    void searchItemTest() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(itemRepository.findAllByAvailableAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(
                eq(true), eq("item"), eq("item")))
                .thenReturn(List.of(item));

        List<ItemDto> result = itemService.searchItem("item");

        assertEquals(1, result.size());
        ItemDto itemDtoTest = result.get(0);
        assertEquals(itemDtoTest.getId(), item.getId());
        assertEquals(itemDtoTest.getDescription(), item.getDescription());
        assertEquals(itemDtoTest.getAvailable(), item.getAvailable());
        assertEquals(itemDtoTest.getRequestId(), item.getRequest().getId());

    }

    @Test
    void createCommentTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(bookingOne)).thenReturn(bookingOne);
        when(bookingRepository.getBookingByBookerAndItem(eq(item.getId()), eq(user.getId()))).thenReturn(List.of(bookingOne));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto commentDtoTest = itemService.createComment(user.getId(), item.getId(), comment);

        assertEquals(commentDtoTest.getId(), comment.getId());
        assertEquals(commentDtoTest.getText(), comment.getText());
        assertEquals(commentDtoTest.getAuthorName(), comment.getAuthor().getName());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createItemWithNonExistingRequestTest() {
        ItemDtoIn itemDtoIn = ItemDtoIn.builder().requestId(123L).build();

        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(LackOfRequestedDataException.class, () -> itemService.createItem(456L, itemDtoIn));
    }

    @Test
    void createItemWithNonExistingUserTest() {
        ItemDtoIn itemDtoIn = ItemDtoIn.builder().requestId(123L).build();


        ItemRequest itemRequest = new ItemRequest();

        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(LackOfRequestedDataException.class, () -> itemService.createItem(456L, itemDtoIn));
    }

    @Test
    void updateItemWithNonExistingItemTest() {
        Item item = new Item();
        Long idOfUserBeingEdited = 123L;

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> itemService.updateItem(456L, itemDtoIn, idOfUserBeingEdited));
    }

    @Test
    void updateItemWithNonExistingUserTest() {
        Item item = new Item();
        Long idOfUserBeingEdited = 123L;

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> itemService.updateItem(456L, itemDtoIn, idOfUserBeingEdited));
    }

    @Test
    void updateItemWithInvalidOwnerTest() {
        User owner = new User();
        owner.setId(1L);

        Item existingItem = new Item();
        existingItem.setId(1L);
        existingItem.setName("Existing Item");
        existingItem.setOwner(owner);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));

        User differentUser = new User();
        differentUser.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> itemService.updateItem(1L, new ItemDtoIn(), differentUser.getId()));
    }

    @Test
    void getItemByIdWhenItemNotFoundTest() {
        long itemId = 1L;
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> itemService.getItemById(itemId, userId));

        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void getItemByUserWhenUserNotFoundTest() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> itemService.getItemByUser(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createCommentWithExceptionsTest() {
        long itemId = 1L;
        long userId = 2L;
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(bookingRepository.getBookingByBookerAndItem(eq(itemId), eq(userId))).thenReturn(Collections.emptyList());

        assertThrows(LackOfRequestedDataException.class, () -> itemService.createComment(itemId, userId, comment));
        verify(itemRepository, times(1)).findById(itemId);

        assertThrows(LackOfRequestedDataException.class, () -> itemService.createComment(itemId, userId, comment));
        assertThrows(LackOfRequestedDataException.class, () -> itemService.createComment(itemId, userId, comment));
    }


    @Test
    void createBookingInvalidItemDataLackOfRequestedDataExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().itemId(1L).build();

        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> bookingService.createBooking(bookingDtoIn, 2L));
    }


    @Test
    void createBookingInvalidUserDataLackOfRequestedDataExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().build();

        assertThrows(LackOfRequestedDataException.class, () -> bookingService.createBooking(bookingDtoIn, 2L));
    }

    @Test
    void createBookingOwnerCreatingBookingLackOfRequestedDataExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder().itemId(1L).userId(1L).build();

        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(item));

        assertThrows(LackOfRequestedDataException.class, () -> bookingService.createBooking(bookingDtoIn, 1L));
    }

    @Test
    void createBookingItemNotAvailableBookingUnavailableItemExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .userId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User user = new User();
        user.setId(2L);
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(false);
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(item));

        assertThrows(BookingUnavailableItemException.class, () -> bookingService.createBooking(bookingDtoIn, 2L));
    }

    @Test
    void createBookingUserNotFoundThrowsExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .userId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, 2L));
    }

    @Test
    void createBookingItemNotFoundThrowsExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .userId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User user = new User();
        user.setId(2L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class,
                () -> bookingService.createBooking(bookingDtoIn, 2L));
    }

    @Test
    void createBookingItemNotAvailableThrowsExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .userId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User user = new User();
        user.setId(2L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(false);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(BookingUnavailableItemException.class,
                () -> bookingService.createBooking(bookingDtoIn, 2L));
    }

    @Test
    void createBookingInvalidDateThrowsExceptionTest() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .userId(2L)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        User user = new User();
        user.setId(2L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(InvalidDataEnteredException.class,
                () -> bookingService.createBooking(bookingDtoIn, 2L));
    }

    @Test
    public void updateItemItemNotFoundTest() {
        Long idItem = 1L;
        Item item = new Item();
        Long idOfUserBeingEdited = 2L;

        when(itemRepository.findById(idItem)).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () ->
                itemService.updateItem(idItem, itemDtoIn, idOfUserBeingEdited));

        verify(itemRepository, times(1)).findById(idItem);
        verifyNoMoreInteractions(itemRepository, userRepository);
    }

    @Test
    void createCommentWithInvalidUserIdTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            itemService.createComment(1L, 2L, new Comment());
        });
    }

    @Test
    void createCommentWithNoBookingsTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.getBookingByBookerAndItem(anyLong(), anyLong())).thenReturn(Collections.emptyList());

        assertThrows(BookingUnavailableItemException.class, () -> {
            itemService.createComment(1L, 2L, new Comment());
        });
    }

    @Test
    void updateItemWithInvalidItemIdTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            itemService.updateItem(1L, new ItemDtoIn(), 2L);
        });
    }

    @Test
    void updateItemWithInvalidUserIdTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            itemService.updateItem(1L, new ItemDtoIn(), 2L);
        });
    }

    @Test
    void updateItemWithMismatchedOwnerIdTest() {
        Item existingItem = new Item();
        existingItem.setOwner(new User());
        existingItem.getOwner().setId(2L);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(existingItem));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(InvalidDataEnteredException.class, () -> {
            itemService.updateItem(1L, new ItemDtoIn(), 3L);
        });
    }

    @Test
    void bookingConfirmationWithNonWaitingStatusTest() {
        Booking existingBooking = new Booking();
        existingBooking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(existingBooking));

        assertThrows(InvalidDataEnteredException.class, () -> {
            bookingService.bookingConfirmation(1L, 2L, true);
        });
    }

    @Test
    void bookingConfirmationWithInvalidUserIdTest() {
        Booking existingBooking = new Booking();
        existingBooking.setStatus(BookingStatus.WAITING);
        existingBooking.setItem(new Item());
        existingBooking.getItem().setOwner(new User());
        existingBooking.getItem().getOwner().setId(3L);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(existingBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            bookingService.bookingConfirmation(1L, 2L, true);
        });
    }


    @Test
    void getUserBookingsTest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Booking> emptyBookingList = Collections.emptyList();
        when(bookingRepository.findAllBookingsByUserIdUser(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findUserBookingsByPast(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findUserBookingsByFuture(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findUserBookingsByCurrent(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findUserBookingsByWatting(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findUserBookingsByRejected(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);

        assertThrows(InvalidDataEnteredException.class,
                () -> bookingService.getUserBookings("ALL", 1L, -1, 0));
        assertThrows(InvalidDataEnteredException.class,
                () -> bookingService.getUserBookings("ALL", 1L, 0, 0));

        assertEquals(0, bookingService.getUserBookings("ALL", 1L, 0, 1).size());
        assertEquals(0, bookingService.getUserBookings("PAST", 1L, 0, 1).size());
        assertEquals(0, bookingService.getUserBookings("FUTURE", 1L, 0, 1).size());
        assertEquals(0, bookingService.getUserBookings("CURRENT", 1L, 0, 1).size());
        assertEquals(0, bookingService.getUserBookings("WAITING", 1L, 0, 1).size());
        assertEquals(0, bookingService.getUserBookings("REJECTED", 1L, 0, 1).size());

        assertThrows(InvalidDataEnteredException.class,
                () -> bookingService.getUserBookings("UNKNOWN_STATE", 1L, 0, 1));

        verify(bookingRepository, times(1)).findAllBookingsByUserIdUser(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findUserBookingsByPast(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findUserBookingsByFuture(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, times(1)).findUserBookingsByCurrent(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findUserBookingsByWatting(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findUserBookingsByRejected(anyLong(), any(PageRequest.class));
    }

    @Test
    void getOwnerBookingsTest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Booking> emptyBookingList = Collections.emptyList();
        when(bookingRepository.findAllBookingsByOwnerIdOwner(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findOwnerBookingsByPast(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findOwnerBookingsByFuture(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findOwnerBookingsByCurrent(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findOwnerBookingsByWatting(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);
        when(bookingRepository.findOwnerBookingsByRejected(anyLong(), any(PageRequest.class))).thenReturn(emptyBookingList);

        assertThrows(InvalidDataEnteredException.class,
                () -> bookingService.getOwnerBookings("ALL", 1L, -1, 0));
        assertThrows(InvalidDataEnteredException.class,
                () -> bookingService.getOwnerBookings("ALL", 1L, 0, 0));

        assertEquals(0, bookingService.getOwnerBookings("ALL", 1L, 0, 1).size());
        assertEquals(0, bookingService.getOwnerBookings("PAST", 1L, 0, 1).size());
        assertEquals(0, bookingService.getOwnerBookings("FUTURE", 1L, 0, 1).size());
        assertEquals(0, bookingService.getOwnerBookings("CURRENT", 1L, 0, 1).size());
        assertEquals(0, bookingService.getOwnerBookings("WAITING", 1L, 0, 1).size());
        assertEquals(0, bookingService.getOwnerBookings("REJECTED", 1L, 0, 1).size());

        assertThrows(NonExistingStatusException.class,
                () -> bookingService.getOwnerBookings("UNKNOWN_STATE", 1L, 0, 1));

        verify(bookingRepository, times(1)).findAllBookingsByOwnerIdOwner(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findOwnerBookingsByPast(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findOwnerBookingsByFuture(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, times(1)).findOwnerBookingsByCurrent(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findOwnerBookingsByWatting(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1)).findOwnerBookingsByRejected(anyLong(), any(PageRequest.class));
    }

}
