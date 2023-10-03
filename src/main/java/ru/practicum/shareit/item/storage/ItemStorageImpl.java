package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemDto.toItemDto;

@Component("itemInMemmory")
public class ItemStorageImpl implements ItemStorage {
    private final Map<Integer, Item> idAndItem = new HashMap<>();
    private int nextId = 1;

    @Override
    public Item addItem(int idUser, Item item) {
        item.setOwner(idUser);
        item.setId(nextId);
        nextId++;
        idAndItem.put(item.getId(), item);
        return toItemDto(item);
    }

    @Override
    public Item updateItem(int idItem, Item item, int idOfUserBeingEdited) {
        if (idAndItem.get(idItem).getOwner() != idOfUserBeingEdited) {
            throw new InvalidDataEnteredException(String.format("У предмета с id = %d не совпадает id владельца = %d",
                    idItem, idOfUserBeingEdited));
        }
        item.setId(idItem);
        createUpdateItem(item);
        idAndItem.put(item.getId(), item);
        return toItemDto(item);
    }

    @Override
    public Item getItemById(int itemId) {
        if (!idAndItem.containsKey(itemId)) throw new LackOfRequestedDataException(
                String.format("Предмета с id = %s не существует", itemId)
        );
        return toItemDto(idAndItem.get(itemId));
    }

    @Override
    public List<Item> getItemByUser(int userId) {
        return idAndItem.values().stream()
                .filter(item -> item.getOwner() == userId)
                .map(ItemDto::toItemDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), itemList -> {
                    if (itemList.isEmpty()) {
                        throw new LackOfRequestedDataException(String.format("У пользователся id = %s нету предметов", userId));
                    }
                    return itemList;
                }));
    }

    @Override
    public List<Item> searchItem(String textQuery) {
        if (textQuery.isBlank()) return new ArrayList<>();
        return idAndItem.values().stream().filter(item ->
                        ((item.getName().toLowerCase().contains(textQuery.toLowerCase())
                                || (item.getDescription().toLowerCase().contains(textQuery.toLowerCase())))
                                && item.getAvailable()))
                .map(ItemDto::toItemDto)
                .collect(Collectors.toList());
    }

    /*
        Создание обновленного предмета
     */
    private void createUpdateItem(Item newItem) {
        Item oldItem = idAndItem.get(newItem.getId());
        if (newItem.getName() == null) newItem.setName(oldItem.getName());
        if (newItem.getDescription() == null) newItem.setDescription(oldItem.getDescription());
        if (newItem.getAvailable() == null) newItem.setAvailable(oldItem.getAvailable());
        newItem.setOwner(oldItem.getOwner());
        newItem.setRequest(oldItem.getRequest());
    }
}
