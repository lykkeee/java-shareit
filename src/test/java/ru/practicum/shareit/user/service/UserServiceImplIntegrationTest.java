package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void updateUser() {
        UserRequestDto user = new UserRequestDto();
        user.setName("name");
        user.setEmail("e@s.com");
        UserResponseDto user1 = userService.createUser(user);

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("nameU");
        user1 = userService.updateUser(user1.getId(), userUpdateDto);

        assertEquals("nameU", user1.getName());

        userUpdateDto.setEmail("e@sU.com");
        user1 = userService.updateUser(user1.getId(), userUpdateDto);

        assertEquals("e@sU.com", user1.getEmail());
    }
}