package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryUserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailUniqueSet = new HashSet<>();
    private long id = 0L;

    public User createUser(User user) {
        final String email = user.getEmail();
        if (emailUniqueSet.contains(email)) {
            throw new EmailAlreadyExistsException("Данный email уже существует: " + email);
        }
        user.setId(++id);
        users.put(user.getId(), user);
        emailUniqueSet.add(email);
        return user;
    }

    public User updateUser(User user) {
        final String email = user.getEmail();
        users.computeIfPresent(user.getId(), (id, u) -> {
            if (!email.equals(u.getEmail())) {
                if (emailUniqueSet.contains(email)) {
                    throw new EmailAlreadyExistsException("Данный email уже существует: " + email);
                }
                emailUniqueSet.remove(u.getEmail());
                emailUniqueSet.add(email);
            }
            return user;
        });
        users.put(user.getId(), user);
        return user;
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(Long id) {
        emailUniqueSet.remove(getUser(id).getEmail());
        users.remove(id);
    }
}
