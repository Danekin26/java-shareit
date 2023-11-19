package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
    Объект запроса на входе
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDtoIn {

    @NotBlank
    @NotNull
    private String description;

}
