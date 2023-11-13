package ru.practicum.shareit.item.dto.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

/*
    Маппер предметов
*/
public class ItemDtoMapper {
    public static ItemDto itemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item dtoToItem(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .owner(owner)
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static List<ItemDto> allTemByAllItemDto(List<Item> allItem) {
        List<ItemDto> allItemDto = new ArrayList<>();
        for (Item item : allItem) {
            allItemDto.add(itemToDto(item));
        }
        return allItemDto;
    }
}
