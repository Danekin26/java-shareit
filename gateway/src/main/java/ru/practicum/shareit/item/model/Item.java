package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/*
    Сущность предмета
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id; // id вещи
    private String name; // имя вещи
    private String description; // описание вещи
    private Boolean available; // статус доступности вещи
    private User owner; // владелец вещи
    private ItemRequest request; // ссылка на созданную вещь по запросу
}
