package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

/*
    Объект запроса на выходе
 */
@Data
@Builder
public class ItemRequestDtoOut {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
