package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @Test
    @SneakyThrows
    void addBooking() {
        Long id = 0L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(id);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);

        when(bookingService.addBooking(id, bookingRequestDto)).thenReturn(bookingResponseDto);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingResponseDto), result);
    }

    @Test
    @SneakyThrows
    void approveBooking() {
        Long id = 0L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(id);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setStatus(Status.APPROVED);

        bookingService.addBooking(id, bookingRequestDto);
        when(bookingService.approveBooking(id, id, true)).thenReturn(bookingResponseDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", id)
                        .header("X-Sharer-User-Id", id)
                        .param("approved", "true")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingResponseDto), result);
    }

    @Test
    @SneakyThrows
    void getBooking() {
        Long id = 0L;

        mockMvc.perform(get("/bookings/{bookingId}", id)
                        .header("X-Sharer-User-Id", id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getBooking(id, id);
    }

    @Test
    @SneakyThrows
    void getUserBookings() {
        Long id = 0L;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", id)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getUserBookings(id, "ALL", 0, 10);
    }

    @Test
    @SneakyThrows
    void getOwnerBookings() {
        Long id = 0L;

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", id)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getOwnerBookings(id, "ALL", 0, 10);
    }
}