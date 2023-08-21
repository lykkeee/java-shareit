package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(Integer id, UserDto user);

    User getUser(Integer id);

    List<User> getUsers();

    void deleteUser(Integer id);
}
