package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDtoOut firstItemRequestDto;

    private ItemRequestDtoOut secondItemRequestDto;

    @BeforeEach
    void beforeEach() {

        firstItemRequestDto = ItemRequestDtoOut.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(LocalDateTime.now())
                .build();


        secondItemRequestDto = ItemRequestDtoOut.builder()
                .id(2L)
                .description("ItemRequest 2")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void addRequestTest() throws Exception {
        LocalDateTime creationTime = LocalDateTime.now();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(creationTime)
                .build();

        ItemRequestDtoOut expectedDto = ItemRequestDtoOut.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(creationTime)
                .build();

        when(itemRequestService.createRequest(anyLong(), any(ItemRequest.class)))
                .thenReturn(expectedDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(expectedDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(expectedDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(expectedDto.getDescription()), String.class));

        verify(itemRequestService, times(1)).createRequest(1L, itemRequest);
    }


    @Test
    void getRequestsTest() throws Exception {
        when(itemRequestService.getRequestsUser(anyLong())).thenReturn(List.of(firstItemRequestDto, secondItemRequestDto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstItemRequestDto, secondItemRequestDto))));

        verify(itemRequestService, times(1)).getRequestsUser(1L);
    }

    @Test
    void getAllRequestsTest() throws Exception {
        when(itemRequestService.getRequestsCreatedOtherUsers(anyLong(), anyInt(), anyInt())).thenReturn(List.of(firstItemRequestDto, secondItemRequestDto));

        mvc.perform(get("/requests/all")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstItemRequestDto, secondItemRequestDto))));

        verify(itemRequestService, times(1)).getRequestsCreatedOtherUsers(1L, 0, 10);
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(itemRequestService.getRequestDtoOut(anyLong(), anyLong())).thenReturn(firstItemRequestDto);

        mvc.perform(get("/requests/{requestId}", firstItemRequestDto.getId())
                        .content(mapper.writeValueAsString(firstItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(firstItemRequestDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(firstItemRequestDto.getDescription()), String.class));

        verify(itemRequestService, times(1)).getRequestDtoOut(1L, 1L);
    }
}