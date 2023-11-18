package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoIn;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/*
    Интерфейс сервиса для управления сущностью предмета
 */
public interface ItemService {

    /*
        Создать предмет
     */
    ItemDto createItem(Long idOwner, ItemDtoIn item);

    /*
        Обновить предмет
     */
    ItemDto updateItem(Long idItem, Item item, Long idOfUserBeingEdited);

    /*
        Получить предмет по id
     */
    ItemDto getItemById(Long itemId, Long idUser);

    /*
        Получить предметы по id владельца
     */
    List<ItemDto> getItemByUser(Long userId);

    /*
        Поиск предмета по названию и описанию
     */
    List<ItemDto> searchItem(String textQuery);

    /*
        Создать отзыв
     */
    CommentDto createComment(Long itemId, Long userId, Comment comment);
}
