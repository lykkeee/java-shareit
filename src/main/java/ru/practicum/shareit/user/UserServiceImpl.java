package ru.practicum.shareit.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public User updateUser(Long id, UserDto userDto) {
        User user = userRepository.getUser(id);
        if (userDto.getEmail() == null) {
            userDto.setEmail(user.getEmail());
        }
        if (userDto.getName() == null) {
            userDto.setName(user.getName());
        }
        userDto.setId(id);
        user = mapper.map(userDto, User.class);
        return userRepository.updateUser(user);
    }

    public User getUser(Long id) {
        return userRepository.getUser(id);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }
}
