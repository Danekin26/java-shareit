package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    /*
        Добавить предмет
     */
    Item addItem(int idUser, Item item);

    /*
        Обновить предмет
    */
    Item updateItem(int idItem, Item item, int idOfUserBeingEdited);

    /*
        Получить предмет по id
    */
    Item getItemById(int itemId);

    /*
        Получить все предметы пользователя
    */
    List<Item> getItemByUser(int userId);

    /*
        Найти предмет по отрезку фразы
    */
    List<Item> searchItem(String textQuery);
}