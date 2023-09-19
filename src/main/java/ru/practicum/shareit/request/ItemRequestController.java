package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@AllArgsConstructor
public class ItemRequestController {

    public final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ItemRequestResponseDto addRequest(@RequestBody @Valid ItemRequestRequestDto requestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на добавление нового запроса на вещь от пользователя с id: {}", userId);
        ItemRequestResponseDto addedRequest = itemRequestService.addRequest(requestDto, userId);
        log.info("Запрос добавлен: {}", addedRequest);
        return addedRequest;
    }

    @GetMapping
    public List<ItemRequestItemDto> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение списка запросов пользователя с id: {}", userId);
        List<ItemRequestItemDto> requests = itemRequestService.getRequests(userId);
        log.info("Список получен");
        return requests;
    }

    @GetMapping("/all")
    public List<ItemRequestItemDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка запросов");
        List<ItemRequestItemDto> requests = itemRequestService.getAllRequests(userId, from, size);
        log.info("Список получен");
        return requests;
    }

    @GetMapping("{requestId}")
    public ItemRequestItemDto getRequest(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение запроса с id: {}", requestId);
        ItemRequestItemDto request = itemRequestService.getRequest(requestId, userId);
        log.info("Запрос получен: {}", request);
        return request;

    }
}
