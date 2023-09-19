package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {
    public final ItemServiceImpl itemService;

    @PostMapping
    public ItemResponseDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemRequestDto item) {
        log.info("Запрос на добавление новой вещи: {}", item);
        ItemResponseDto addedItem = itemService.addItem(userId, item);
        log.info("Вещь добавлена: {}", addedItem);
        return addedItem;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@PathVariable("itemId") Long itemId, @RequestBody ItemUpdateDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на обновление вещи: {}", item);
        ItemResponseDto updatedItem = itemService.updateItem(itemId, item, userId);
        log.info("Вещь обновлена: {}", updatedItem);
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение вещи с id: {}", itemId);
        ItemResponseDto item = itemService.getItem(itemId, userId);
        log.info("Вещь получена: {}", item);
        return item;
    }

    @GetMapping
    public List<ItemResponseDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение всех вещей владельца с id: {}", userId);
        List<ItemResponseDto> items = itemService.getOwnerItems(userId, from, size);
        log.info("Список вещей получен");
        return items;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> getSearchedItems(@RequestParam String text,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение вещей по результатам поиска: {}", text);
        List<ItemResponseDto> items = itemService.getSearchedItems(text, from, size);
        log.info("Список вещей получен");
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId, @RequestBody @Valid CommentRequestDto comment) {
        log.info("Запрос на добавление комментария к вещи с id: {} от пользователя с id: {}", itemId, userId);
        CommentResponseDto addedComment = itemService.addComment(userId, itemId, comment);
        log.info("Комментарий добавлен");
        return addedComment;
    }
}
