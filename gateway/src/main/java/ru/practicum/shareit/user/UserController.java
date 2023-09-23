package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("Запрос на создание пользователя: {}", userRequestDto);
        return userClient.createUser(userRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") @Positive Long id,
                                             @RequestBody UserUpdateDto userUpdateDto) {
        log.info("Запрос на обновление пользователя с id: {}", id);
        return userClient.updateUser(id, userUpdateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") @Positive Long id) {
        log.info("Запрос на получение пользователя с id: {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return userClient.getUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") @Positive Long id) {
        log.info("Запрос на удаление пользователя с id: {}", id);
        return userClient.deleteUser(id);
    }
}
