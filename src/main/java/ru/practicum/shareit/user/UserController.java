package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Запрос на создание пользователя: {}", user);
        User createdUser = userService.createUser(user);
        log.info("Пользователь создан: {}", createdUser);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Integer id, @RequestBody UserDto user) {
        log.info("Запрос на обновление пользователя с id: {}", id);
        User updatedUser = userService.updateUser(id, user);
        log.info("Пользователь обновлён: {}", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        log.info("Запрос на получение пользователя с id: {}", id);
        User user = userService.getUser(id);
        log.info("Пользователь получен: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Запрос на получение всех пользователей");
        List<User> users = userService.getUsers();
        log.info("Пользователи получены");
        return users;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        log.info("Запрос на удаление пользователя с id: {}", id);
        userService.deleteUser(id);
        log.info("Пользователь удалён");
    }
}
