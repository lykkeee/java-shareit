package ru.practicum.shareit.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public UserResponseDto createUser(UserRequestDto user) {
        User createdUser = mapper.map(user, User.class);
        return mapper.map(userRepository.save(createdUser), UserResponseDto.class);
    }

    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с данным id не найден: " + id));
        if (userUpdateDto.getEmail() == null) {
            userUpdateDto.setEmail(user.getEmail());
        }
        if (userUpdateDto.getName() == null) {
            userUpdateDto.setName(user.getName());
        }
        userUpdateDto.setId(id);
        user = mapper.map(userUpdateDto, User.class);
        return mapper.map(userRepository.save(user), UserResponseDto.class);
    }

    public UserResponseDto getUser(Long id) {
        return mapper.map((userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с данным id не найден: " + id))), UserResponseDto.class);
    }

    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(user -> mapper.map(user, UserResponseDto.class)).collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
