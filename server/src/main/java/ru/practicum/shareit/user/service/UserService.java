package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto user);

    UserResponseDto updateUser(Long id, UserUpdateDto user);

    UserResponseDto getUser(Long id);

    List<UserResponseDto> getUsers();

    void deleteUser(Long id);
}
