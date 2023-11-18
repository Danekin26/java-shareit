package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoIn;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemServiceImpl itemService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createItemTest() throws Exception {
        ItemDtoIn itemDtoIn = ItemDtoIn.builder()
                .name("Test Item")
                .description("This is a test item.")
                .available(true)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .build();

        when(itemService.createItem(anyLong(), any(ItemDtoIn.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDtoIn.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoIn.getDescription())));

        verify(itemService, times(1)).createItem(anyLong(), any(ItemDtoIn.class));
    }

    @Test
    void updateItemTest() throws Exception {
        Item item = new Item();
        item.setName("Updated Item");
        item.setDescription("This is an updated item.");

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name(item.getName())
                .description(item.getDescription())
                .build();

        when(itemService.updateItem(anyLong(), any(Item.class), anyLong())).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())));

        verify(itemService, times(1)).updateItem(anyLong(), any(Item.class), anyLong());
    }

    @Test
    void getItemByIdTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("This is a test item.")
                .build();

        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(get("/items/{itemId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemService, times(1)).getItemById(anyLong(), anyLong());
    }

    @Test
    void getItemByUserTest() throws Exception {
        List<ItemDto> itemDtoList = List.of(
                ItemDto.builder().id(1L).name("Item 1").description("Description 1").build(),
                ItemDto.builder().id(2L).name("Item 2").description("Description 2").build()
        );

        when(itemService.getItemByUser(anyLong())).thenReturn(itemDtoList);

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Item 1")))
                .andExpect(jsonPath("$[0].description", is("Description 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Item 2")))
                .andExpect(jsonPath("$[1].description", is("Description 2")));

        verify(itemService, times(1)).getItemByUser(anyLong());
    }

    @Test
    void searchItemTest() throws Exception {
        List<ItemDto> itemDtoList = List.of(
                ItemDto.builder().id(1L).name("Item 1").description("Description 1").build(),
                ItemDto.builder().id(2L).name("Item 2").description("Description 2").build()
        );

        when(itemService.searchItem(anyString())).thenReturn(itemDtoList);

        mockMvc.perform(get("/items/search")
                        .param("text", "test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Item 1")))
                .andExpect(jsonPath("$[0].description", is("Description 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Item 2")))
                .andExpect(jsonPath("$[1].description", is("Description 2")));

        verify(itemService, times(1)).searchItem(anyString());
    }

    @Test
    void createCommentTest() throws Exception {
        Comment comment = new Comment();
        comment.setText("This is a test comment.");

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text(comment.getText())
                .build();

        when(itemService.createComment(anyLong(), anyLong(), any(Comment.class))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is(comment.getText())));

        verify(itemService, times(1)).createComment(anyLong(), anyLong(), any(Comment.class));
    }
}
