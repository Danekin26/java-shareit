package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
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

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingServiceImpl bookingService;

    @Test
    void createBookingTest() throws Exception {
        BookingDtoIn bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setItemId(1L);
        LocalDateTime start = LocalDateTime.now();
        bookingDtoIn.setStart(start);
        bookingDtoIn.setEnd(start.plusHours(1));

        BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
                .id(1L)
                .start(start)
                .end(bookingDtoIn.getEnd())
                .build();

        when(bookingService.createBooking(any(BookingDtoIn.class), anyLong())).thenReturn(bookingDtoOut);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is(start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoIn.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

        verify(bookingService, times(1)).createBooking(any(BookingDtoIn.class), anyLong());
    }


    @Test
    void bookingConfirmationTest() throws Exception {
        BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .build();

        when(bookingService.bookingConfirmation(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDtoOut);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is(bookingDtoOut.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoOut.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

        verify(bookingService, times(1)).bookingConfirmation(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getBookingTest() throws Exception {
        BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .build();

        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(bookingDtoOut);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is(bookingDtoOut.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoOut.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

        verify(bookingService, times(1)).getBooking(anyLong(), anyLong());
    }

    @Test
    void getUserBookingsTest() throws Exception {
        List<BookingDtoOut> bookingDtoOutList = List.of(
                BookingDtoOut.builder().id(1L).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1)).build(),
                BookingDtoOut.builder().id(2L).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(2)).build()
        );

        when(bookingService.getUserBookings(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(bookingDtoOutList);

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].start", is(bookingDtoOutList.get(0).getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", is(bookingDtoOutList.get(0).getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].start", is(bookingDtoOutList.get(1).getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].end", is(bookingDtoOutList.get(1).getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

        verify(bookingService, times(1)).getUserBookings(anyString(), anyLong(), anyInt(), anyInt());
    }

    @Test
    void getOwnerBookingsTest() throws Exception {
        List<BookingDtoOut> bookingDtoOutList = List.of(
                BookingDtoOut.builder().id(1L).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1)).build(),
                BookingDtoOut.builder().id(2L).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(2)).build()
        );

        when(bookingService.getOwnerBookings(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(bookingDtoOutList);

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].start", is(bookingDtoOutList.get(0).getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", is(bookingDtoOutList.get(0).getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].start", is(bookingDtoOutList.get(1).getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].end", is(bookingDtoOutList.get(1).getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

        verify(bookingService, times(1)).getOwnerBookings(anyString(), anyLong(), anyInt(), anyInt());
    }
}