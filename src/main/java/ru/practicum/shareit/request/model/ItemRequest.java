package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequest {
    private Integer id; // id запроса
    private String description; // описание запроса требуемой вещи
    private User requestor; // пользователь, создавший запрос
    private LocalDateTime created; // дата создания
}
