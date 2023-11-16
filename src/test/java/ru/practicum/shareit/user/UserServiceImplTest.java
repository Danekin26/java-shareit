package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

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
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();
    }

    @Test
    void createUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDtoTest = userService.createUser(user);

        assertEquals(userDtoTest.getId(), user.getId());
        assertEquals(userDtoTest.getName(), user.getName());
        assertEquals(userDtoTest.getEmail(), user.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUserTest() {
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).getReferenceById(anyLong());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void updateUserTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User updatedUser = User.builder()
                .id(1L)
                .name("UpdatedAnna")
                .email("updatedanna@yandex.ru")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto userDtoTest = userService.updateUser(user.getId(), updatedUser);

        assertEquals(userDtoTest.getId(), updatedUser.getId());
        assertEquals(userDtoTest.getName(), updatedUser.getName());
        assertEquals(userDtoTest.getEmail(), updatedUser.getEmail());

        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllUsersTest() {
        List<User> userList = List.of(user);
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> userDtoList = userService.getAllUsers();

        assertEquals(userDtoList.size(), userList.size());
        assertEquals(userDtoList.get(0).getId(), userList.get(0).getId());
        assertEquals(userDtoList.get(0).getName(), userList.get(0).getName());
        assertEquals(userDtoList.get(0).getEmail(), userList.get(0).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto userDtoTest = userService.getUserById(user.getId());

        assertEquals(userDtoTest.getId(), user.getId());
        assertEquals(userDtoTest.getName(), user.getName());
        assertEquals(userDtoTest.getEmail(), user.getEmail());

        verify(userRepository, times(1)).findById(anyLong());
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
    void updateUserExceptionTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LackOfRequestedDataException.class, () -> {
            userService.updateUser(222L, new User());
        });

        verify(userRepository, times(1)).findById(eq(222L));
    }
}