package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class RequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDtoOut> json;

    @Test
    void itemRequestDtoTest() throws Exception {

        LocalDateTime created = LocalDateTime.of(2023, 8, 4, 0, 0);

        ItemDto itemDto = ItemDto.builder()
                .requestId(1L)
                .name("guitar")
                .description("a very good tool")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        ItemRequestDtoOut itemRequestDto = ItemRequestDtoOut.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(created)
                .items(List.of(itemDto))
                .build();

        JsonContent<ItemRequestDtoOut> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("ItemRequest 1");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}