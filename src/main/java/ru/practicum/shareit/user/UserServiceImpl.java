package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private int id = 1;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public User createUser(User user) {
        for (User user1 : userRepository.getUsers()) {
            if (Objects.equals(user.getEmail(), user1.getEmail())) {
                throw new EmailAlreadyExistsException("Пользователь с таким email уже существует: " + user.getEmail());
            }
        }
        user.setId(id++);
        return userRepository.createUser(user);
    }

    public User updateUser(Integer id, UserDto user) {
        for (User user1 : userRepository.getUsers()) {
            if (Objects.equals(user.getEmail(), user1.getEmail()) && !Objects.equals(user1.getId(), id)) {
                throw new EmailAlreadyExistsException("Пользователь с таким email уже существует: " + user.getEmail());
            }
            if (Objects.equals(id, user1.getId())) {
                if (user.getEmail() == null) {
                    user.setEmail(user1.getEmail());
                }
                if (user.getName() == null) {
                    user.setName(user1.getName());
                }
            }
        }
        user.setId(id);
        User user1 = mapper.map(user, User.class);
        return userRepository.updateUser(user1);
    }

    public User getUser(Integer id) {
        return userRepository.getUser(id);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public void deleteUser(Integer id) {
        userRepository.deleteUser(id);
    }
}
