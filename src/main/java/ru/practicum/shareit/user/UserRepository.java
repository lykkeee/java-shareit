package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository {
    private final Map<Integer, User> userMap = new HashMap<>();

    public User createUser(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public User getUser(Integer id) {
        return userMap.get(id);
    }

    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    public void deleteUser(Integer id) {
        userMap.remove(id);
    }
}
