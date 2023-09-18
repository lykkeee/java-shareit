package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    @Test
    @SneakyThrows
    void addRequest() {
        Long id = 0L;
        String text = "desc";

        ItemRequestRequestDto itemRequestRequestDto = new ItemRequestRequestDto();
        itemRequestRequestDto.setDescription(text);

        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setId(id);
        itemRequestResponseDto.setDescription(text);

        when(itemRequestService.addRequest(itemRequestRequestDto, id)).thenReturn(itemRequestResponseDto);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestResponseDto), result);
    }

    @Test
    @SneakyThrows
    void getRequests() {
        Long id = 0L;

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).getRequests(id);
    }

    @Test
    @SneakyThrows
    void getAllRequests() {
        Long id = 0L;

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).getAllRequests(id, 0, 10);
    }

    @Test
    @SneakyThrows
    void getRequest() {
        Long id = 0L;

        mockMvc.perform(get("/requests/{requestId}", id)
                        .header("X-Sharer-User-Id", id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).getRequest(id, id);
    }
}