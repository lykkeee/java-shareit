package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody @Valid ItemRequestRequestDto requestDto,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос на добавление нового запроса на вещь от пользователя с id: {}", userId);
        return itemRequestClient.addRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос на получение списка запросов пользователя с id: {}", userId);
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение списка запросов");
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable("requestId") @Positive Long requestId,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос на получение запроса с id: {}", requestId);
        return itemRequestClient.getRequest(requestId, userId);
    }
}
