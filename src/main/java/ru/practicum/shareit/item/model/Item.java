package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Item {
    private int id; // id вещи
    @NotNull
    @NotEmpty
    private String name; // имя вещи
    @NotNull
    @NotEmpty
    private String description; // описание вещи
    @NotNull
    private Boolean available; // статус доступности вещи
    private Integer owner; // владелец вещи
    private ItemRequest request; // ссылка на созданную вещь по запросу
}
