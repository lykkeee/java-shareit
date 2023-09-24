package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @SneakyThrows
    void createUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("user");
        userRequestDto.setEmail("a@l.com");

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(0L);
        userResponseDto.setName("user");
        userResponseDto.setEmail("a@l.com");

        when(userService.createUser(userRequestDto)).thenReturn(userResponseDto);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userResponseDto), result);
    }

    @Test
    @SneakyThrows
    void updateUser() {
        Long userId = 0L;

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("user");
        userRequestDto.setEmail("a@l.com");

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("userU");

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(userId);
        userResponseDto.setName("userU");
        userResponseDto.setEmail("a@l.com");

        when(userService.createUser(userRequestDto)).thenReturn(userResponseDto);
        when(userService.updateUser(userId, userUpdateDto)).thenReturn(userResponseDto);

        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userResponseDto), result);
    }

    @Test
    @SneakyThrows
    void getUser() {
        Long userId = 0L;

        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getUser(userId);
    }

    @Test
    @SneakyThrows
    void getUsers() {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getUsers();
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        Long userId = 0L;

        mockMvc.perform(delete("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }

}