package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicationOfExistingDataException;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.request.dto.ItemRequestDtoMapper.allItemRequestToAllItemRequestDtoOut;
import static ru.practicum.shareit.request.dto.ItemRequestDtoMapper.itemRequestDtoInToRequest;
import static ru.practicum.shareit.request.dto.ItemRequestDtoMapper.itemRequestToRequestDtoOut;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDtoOut createRequest(Long userId, ItemRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new DuplicationOfExistingDataException(String.format("Пользователь с id %d не существует", userId)));
        ItemRequest itemRequestDtoOut = itemRequestDtoInToRequest(request.getDescription(), user, LocalDateTime.now());
        if ((itemRequestDtoOut.getDescription() == null) || (itemRequestDtoOut.getDescription().isBlank())
                || (itemRequestDtoOut.getDescription().isEmpty()) || (itemRequestDtoOut.getDescription().equals("null")))
            throw new InvalidDataEnteredException("Описание запроса некорректно");
        return itemRequestToRequestDtoOut(itemRequestRepository.save(itemRequestDtoOut));
    }

    @Override
    public List<ItemRequestDtoOut> getRequestsUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new DuplicationOfExistingDataException(String.format("Пользователь с id %d не существует", userId)));
        List<ItemRequest> allItemRequest = itemRequestRepository.findAllByRequestor_Id(userId);
        for (ItemRequest itemRequest : allItemRequest) {
            setItemRequest(itemRequest);
        }
        return allItemRequestToAllItemRequestDtoOut(allItemRequest);
    }

    @Override
    public List<ItemRequestDtoOut> getRequestsCreatedOtherUsers(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        User user = userRepository
                .findById(userId)
                .orElseThrow(()
                        -> new DuplicationOfExistingDataException(String.format("Пользователь с id %d не существует", userId)));
        if (from < 0 || size < 1) {
            throw new InvalidDataEnteredException("size и from введены не корректно");
        }
        List<ItemRequest> allItemRequest = itemRequestRepository
                .findAllByRequestor_IdNotOrderByCreatedDesc(userId, pageable);
        for (ItemRequest itemRequest : allItemRequest) {
            setItemRequest(itemRequest);
        }
        return allItemRequestToAllItemRequestDtoOut(allItemRequest);
    }

    @Override
    public ItemRequestDtoOut getRequestDtoOut(Long requestId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new DuplicationOfExistingDataException(String.format("Пользователь с id %d не существует", userId)));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Запроса с id %d не существует", requestId)));
        itemRequest.setItems(itemRepository.findAllByRequest_Id(requestId));
        setItemRequest(itemRequest);
        return itemRequestToRequestDtoOut(itemRequest);
    }

    private void setItemRequest(ItemRequest itemRequest) {
        List<Item> allItemRequest = itemRepository.findAllByRequest_Id(itemRequest.getId());
        if (allItemRequest == null) allItemRequest = new ArrayList<>();
        itemRequest.setItems(allItemRequest);
    }

}
