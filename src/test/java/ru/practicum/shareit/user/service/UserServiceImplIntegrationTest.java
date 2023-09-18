package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getUsers() {
        UserRequestDto user = new UserRequestDto();
        user.setName("name");
        user.setEmail("e@s.com");
        UserResponseDto user1 = userService.createUser(user);

        List<UserResponseDto> users = userService.getUsers();

        assertFalse(users.isEmpty());
        assertEquals("name", users.get(0).getName());
    }
}