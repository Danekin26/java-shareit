package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

/*
    Объект сущности для связи с БД запроса
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private Long id; // id запроса
    private String description; // описание запроса требуемой вещи
    private User requestor; // пользователь, создавший запрос
    private LocalDateTime created = LocalDateTime.now(); // дата создания
    private List<Item> items;
}
