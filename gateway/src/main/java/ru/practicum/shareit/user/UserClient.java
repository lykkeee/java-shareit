package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Service
@Slf4j
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserRequestDto userRequestDto) {
        log.info("Запрос на создание пользователя: {}", userRequestDto);
        return post("", userRequestDto);
    }

    public ResponseEntity<Object> updateUser(Long id, UserUpdateDto userUpdateDto) {
        log.info("Запрос на обновление пользователя с id: {}", id);
        return patch("/" + id, userUpdateDto);
    }

    public ResponseEntity<Object> getUser(Long id) {
        log.info("Запрос на получение пользователя с id: {}", id);
        return get("/" + id);
    }

    public ResponseEntity<Object> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return get("");
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        log.info("Запрос на удаление пользователя с id: {}", id);
        return delete("/" + id);
    }
}
