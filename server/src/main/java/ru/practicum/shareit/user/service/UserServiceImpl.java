package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static ru.practicum.shareit.user.dto.UserDtoMapper.allUserToAllUserDto;
import static ru.practicum.shareit.user.dto.UserDtoMapper.userToDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(User user) {
        User savedUser = userRepository.save(user);
        log.info(String.format("Пользователь с email %s и id %d сохранен", user.getEmail(), user.getId()));
        return userToDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User deleteUser = userRepository.getReferenceById(id);
        userRepository.delete(deleteUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, User user) {
        User updatedUser = userRepository.findById(id).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Пользователь с id %d не существует", id)));
        updatedUser.setEmail(user.getEmail() == null ? updatedUser.getEmail() : user.getEmail());
        updatedUser.setName(user.getName() == null ? updatedUser.getName() : user.getName());
        return userToDto(updatedUser);
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        return allUserToAllUserDto(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        return userToDto(userRepository.findById(id).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Пользователь с id %d не существует", id))));
    }
}
