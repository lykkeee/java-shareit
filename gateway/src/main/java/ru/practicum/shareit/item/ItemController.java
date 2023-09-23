package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                          @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Запрос на добавление новой вещи: {}", itemRequestDto);
        return itemClient.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable("itemId") @Positive Long itemId,
                                             @RequestBody ItemUpdateDto itemUpdateDto,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос на обновление вещи: {}", itemUpdateDto);
        return itemClient.updateItem(itemId,itemUpdateDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable("itemId") @Positive Long itemId,
                                          @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос на получение вещи с id: {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение всех вещей владельца с id: {}", userId);
        return itemClient.getOwnerItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchedItems(@RequestParam String text,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение вещей по результатам поиска: {}", text);
        return itemClient.getSearchedItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                         @PathVariable("itemId") @Positive Long itemId,
                                         @RequestBody @Valid CommentRequestDto comment) {
        log.info("Запрос на добавление комментария к вещи с id: {} от пользователя с id: {}", itemId, userId);
        return itemClient.addComment(userId, itemId, comment);
    }
}
