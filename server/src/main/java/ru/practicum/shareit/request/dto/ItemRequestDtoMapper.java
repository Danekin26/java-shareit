package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.item.dto.item.ItemDtoMapper.allTemByAllItemDto;

/*
    Маппер запросов
 */
public class ItemRequestDtoMapper {

    public static ItemRequestDtoOut itemRequestToRequestDtoOut(ItemRequest itemRequest) {
        return ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(allTemByAllItemDto(itemRequest.getItems()))
                .build();
    }

    public static ItemRequest itemRequestDtoInToRequest(String description, User user, LocalDateTime created) {
        return ItemRequest.builder()
                .description(description)
                .requestor(user)
                .created(created)
                .build();
    }

    public static List<ItemRequestDtoOut> allItemRequestToAllItemRequestDtoOut(List<ItemRequest> allItemRequest) {
        List<ItemRequestDtoOut> allItemRequestDtoOut = new ArrayList<>();
        for (ItemRequest itemRequest : allItemRequest) {
            allItemRequestDtoOut.add(itemRequestToRequestDtoOut(itemRequest));
        }
        return allItemRequestDtoOut;
    }
}
