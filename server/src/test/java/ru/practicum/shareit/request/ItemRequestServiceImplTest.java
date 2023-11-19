package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.DuplicationOfExistingDataException;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
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
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRequestServiceImplTest {
    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;
    private User user;
    private ItemRequest itemRequest;
    private ItemRequest otherUserRequestOne;
    private ItemRequest otherUserRequestTwo;
    private ItemRequestDtoOut itemRequestDto;
    private Item item;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1L)
                .name("screwdriver")
                .description("works well, does not ask to eat")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();

        itemRequestDto = ItemRequestDtoOut.builder()
                .description("ItemRequest 1")
                .build();

        otherUserRequestOne = ItemRequest.builder()
                .id(2L)
                .description("Other User Request 1")
                .created(LocalDateTime.now())
                .build();

        otherUserRequestTwo = ItemRequest.builder()
                .id(3L)
                .description("Other User Request 2")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void createRequestTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDtoOut itemRequestDtoTest = itemRequestService.createRequest(user.getId(), itemRequest);

        assertEquals(itemRequestDtoTest.getId(), itemRequest.getId());
        assertEquals(itemRequestDtoTest.getDescription(), itemRequest.getDescription());

        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void getUserByIdExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            userService.getUserById(222L);
        });

        verify(userRepository, times(1)).findById(eq(222L));
    }

    @Test
    void getRequestsUserExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(DuplicationOfExistingDataException.class, () -> itemRequestService.getRequestsUser(222L));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getRequestsCreatedOtherUsersExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new User()));
        when(itemRequestRepository.findAllByRequestor_IdNotOrderByCreatedDesc(anyLong(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(InvalidDataEnteredException.class, () ->
                itemRequestService.getRequestsCreatedOtherUsers(222L, -1, 10));
    }

    @Test
    void getRequestDtoOutUserNotFoundExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DuplicationOfExistingDataException.class, () ->
                itemRequestService.getRequestDtoOut(1L, 222L));
    }

    @Test
    void getRequestDtoOutItemRequestNotFoundExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () ->
                itemRequestService.getRequestDtoOut(222L, 1L));
    }

    @Test
    void updateUserExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            userService.updateUser(222L, new User());
        });

        verify(userRepository, times(1)).findById(eq(222L));
    }

    @Test
    void getRequestsUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequestor_Id(anyLong())).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequest_Id(anyLong())).thenReturn(List.of(item));

        ItemRequestDtoOut itemRequestDtoTest = itemRequestService.getRequestsUser(user.getId()).get(0);

        assertEquals(itemRequestDtoTest.getItems().get(0).getId(), item.getId());
        assertEquals(itemRequestDtoTest.getItems().get(0).getName(), item.getName());
        assertEquals(itemRequestDtoTest.getItems().get(0).getDescription(), item.getDescription());
        assertEquals(itemRequestDtoTest.getItems().get(0).getAvailable(), item.getAvailable());

        verify(itemRequestRepository, times(1)).findAllByRequestor_Id(anyLong());
    }

    @Test
    void getRequestsCreatedOtherUsersTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));


        when(itemRequestRepository.findAllByRequestor_IdNotOrderByCreatedDesc(anyLong(), any(PageRequest.class)))
                .thenReturn(List.of(otherUserRequestOne, otherUserRequestTwo));
        List<ItemRequestDtoOut> result = itemRequestService.getRequestsCreatedOtherUsers(user.getId(), 0, 10);

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRequestRepository, times(1)).findAllByRequestor_IdNotOrderByCreatedDesc(anyLong(), any(PageRequest.class));
    }

    @Test
    void getRequestDtoOutTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequest_Id(anyLong())).thenReturn(List.of(item));

        ItemRequestDtoOut itemRequestDtoTest = itemRequestService.getRequestDtoOut(itemRequest.getId(), user.getId());

        assertEquals(itemRequestDtoTest.getId(), itemRequest.getId());
        assertEquals(itemRequestDtoTest.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDtoTest.getItems().get(0).getId(), item.getId());
        assertEquals(itemRequestDtoTest.getItems().get(0).getName(), item.getName());
        assertEquals(itemRequestDtoTest.getItems().get(0).getDescription(), item.getDescription());
        assertEquals(itemRequestDtoTest.getItems().get(0).getAvailable(), item.getAvailable());

        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRequestRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(2)).findAllByRequest_Id(anyLong());
    }

    @Test
    public void createRequestUserNotFoundTest() {
        Long userId = 1L;
        Mockito.when(userRepository.findById(eq(userId))).thenReturn(java.util.Optional.empty());

        assertThrows(DuplicationOfExistingDataException.class,
                () -> itemRequestService.createRequest(userId, new ItemRequest()));
    }

    @Test
    public void createRequestInvalidDescriptionTest() {
        Long userId = 1L;
        User user = new User();
        Mockito.when(userRepository.findById(eq(userId))).thenReturn(java.util.Optional.of(user));

        assertThrows(InvalidDataEnteredException.class,
                () -> itemRequestService.createRequest(userId, new ItemRequest()));
    }

    @Test
    void getRequestsCreatedOtherUsersInvalidUserIdTest() {
        Long invalidUserId = 999L;
        when(userRepository.findById(invalidUserId)).thenReturn(java.util.Optional.empty());

        assertThrows(DuplicationOfExistingDataException.class, () ->
                itemRequestService.getRequestsCreatedOtherUsers(invalidUserId, 0, 10)
        );
    }
}