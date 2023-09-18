package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userRepository, new ModelMapper());
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("w@l.com");
    }

    @Test
    void createUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("name");
        userRequestDto.setEmail("w@l.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);

        assertEquals(1L, userResponseDto.getId());
        assertEquals("name", userResponseDto.getName());
        assertEquals("w@l.com", userResponseDto.getEmail());
    }

    @Test
    void updateUser() {
        UserUpdateDto request = new UserUpdateDto();
        request.setName("nameU");

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto response = userService.updateUser(user.getId(), request);

        assertEquals("name", response.getName());
    }

    @Test
    void getUser() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));

        UserResponseDto userResponseDto = userService.getUser(user.getId());

        assertEquals(1L, userResponseDto.getId());
        assertEquals("name", userResponseDto.getName());
        assertEquals("w@l.com", userResponseDto.getEmail());
    }
}