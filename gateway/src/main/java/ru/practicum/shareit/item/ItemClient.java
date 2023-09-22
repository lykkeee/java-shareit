package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

@Service
@Slf4j
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(Long userId, ItemRequestDto itemRequestDto) {
        log.info("Запрос на добавление новой вещи: {}", itemRequestDto);
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> updateItem(Long itemId, ItemUpdateDto itemUpdateDto, Long userId) {
        log.info("Запрос на обновление вещи: {}", itemUpdateDto);
        return patch("/" + itemId, userId, itemUpdateDto);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        log.info("Запрос на получение вещи с id: {}", itemId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getOwnerItems(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of("from", from, "size", size);
        log.info("Запрос на получение всех вещей владельца с id: {}", userId);
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getSearchedItems(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of("text", text, "from", from, "size", size);
        log.info("Запрос на получение вещей по результатам поиска: {}", text);
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentRequestDto comment) {
        log.info("Запрос на добавление комментария к вещи с id: {} от пользователя с id: {}", itemId, userId);
        return post("/" + itemId + "/comment", userId, comment);
    }
}
