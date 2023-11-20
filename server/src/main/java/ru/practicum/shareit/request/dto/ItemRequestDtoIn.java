package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/*
    Объект запроса на входе
 */
@Data
@Builder
public class ItemRequestDtoIn {

    @NotBlank
    @NotNull
    @NotEmpty
    private String description;

}
