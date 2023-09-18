package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemServiceImpl itemService;

    @Test
    @SneakyThrows
    void addItem() {
        Long id = 0L;

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item");
        itemRequestDto.setDescription("desc");
        itemRequestDto.setAvailable(true);

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(id);
        itemResponseDto.setName("item");
        itemResponseDto.setDescription("desc");
        itemResponseDto.setAvailable(true);

        when(itemService.addItem(id, itemRequestDto)).thenReturn(itemResponseDto);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemResponseDto), result);
    }

    @Test
    @SneakyThrows
    void updateItem() {
        Long id = 0L;

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item");
        itemRequestDto.setDescription("desc");
        itemRequestDto.setAvailable(true);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemU");

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(id);
        itemResponseDto.setName("itemU");
        itemResponseDto.setDescription("desc");
        itemResponseDto.setAvailable(true);

        when(itemService.addItem(id, itemRequestDto)).thenReturn(itemResponseDto);
        when(itemService.updateItem(id, itemUpdateDto, id)).thenReturn(itemResponseDto);

        String result = mockMvc.perform(patch("/items/{itemId}", id)
                        .header("X-Sharer-User-Id", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemUpdateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemResponseDto), result);
    }

    @Test
    @SneakyThrows
    void getItem() {
        Long id = 0L;

        mockMvc.perform(get("/items/{itemId}", id)
                        .header("X-Sharer-User-Id", id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).getItem(id, id);
    }

    @Test
    @SneakyThrows
    void getOwnerItems() {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 0L))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).getOwnerItems(0L, 0, 10);
    }

    @Test
    @SneakyThrows
    void addComment() {
        Long id = 0L;
        String text = "comment";

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText(text);

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(id);
        commentResponseDto.setText(text);

        when(itemService.addComment(id, id, commentRequestDto)).thenReturn(commentResponseDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", id)
                        .header("X-Sharer-User-Id", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentResponseDto), result);
    }
}