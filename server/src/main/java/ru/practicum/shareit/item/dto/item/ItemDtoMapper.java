package ru.practicum.shareit.item.dto.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

/*
    Маппер предметов
*/
public class ItemDtoMapper {
    public static ItemDto itemToDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId((item.getRequest() == null) ? null : item.getRequest().getId())
                .build();
        return itemDto;
    }

    public static Item itemDtoInToItem(ItemDtoIn itemDtoIn, ItemRequest itemRequest) {
        Item item = Item.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .build();
        if (itemRequest != null) item.setRequest(itemRequest);
        return item;
    }

    public static List<ItemDto> allTemByAllItemDto(List<Item> allItem) {
        List<ItemDto> allItemDto = new ArrayList<>();
        if (allItem == null) return allItemDto;
        for (Item item : allItem) {
            allItemDto.add(itemToDto(item));
        }
        return allItemDto;
    }

    public static ItemDtoIn itemToItemDtoIn(Item item) {
        return ItemDtoIn.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest().getId())
                .build();
    }
}
