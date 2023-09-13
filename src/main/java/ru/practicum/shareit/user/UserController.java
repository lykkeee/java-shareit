package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto user) {
        log.info("Запрос на создание пользователя: {}", user);
        UserResponseDto createdUser = userService.createUser(user);
        log.info("Пользователь создан: {}", createdUser);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateDto user) {
        log.info("Запрос на обновление пользователя с id: {}", id);
        UserResponseDto updatedUser = userService.updateUser(id, user);
        log.info("Пользователь обновлён: {}", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable("id") Long id) {
        log.info("Запрос на получение пользователя с id: {}", id);
        UserResponseDto user = userService.getUser(id);
        log.info("Пользователь получен: {}", user);
        return user;
    }

    @GetMapping
    public List<UserResponseDto> getUsers() {
        log.info("Запрос на получение всех пользователей");
        List<UserResponseDto> users = userService.getUsers();
        log.info("Пользователи получены");
        return users;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        log.info("Запрос на удаление пользователя с id: {}", id);
        userService.deleteUser(id);
        log.info("Пользователь удалён");
    }
}
