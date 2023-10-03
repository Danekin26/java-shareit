package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

/*
    Сервис для управления предметами
 */
@Service
public class ItemService {
    @Qualifier("itemInMemmory")
    private final ItemStorage itemStorage;
    @Qualifier("userInMemmory")
    private final UserStorage userStorage;

    public ItemService(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public Item createItem(int userId, Item item) {
        userStorage.getUserById(userId);
        return itemStorage.addItem(userId, item);
    }

    public Item updateItem(int idItem, Item item, int idOfUserBeingEdited) {
        return itemStorage.updateItem(idItem, item, idOfUserBeingEdited);
    }

    public Item getItemById(int itemId) {
        return itemStorage.getItemById(itemId);
    }

    public List<Item> getItemByUser(int userId) {
        return itemStorage.getItemByUser(userId);
    }

    public List<Item> searchItem(String textQuery) {
        return itemStorage.searchItem(textQuery);
    }
}
