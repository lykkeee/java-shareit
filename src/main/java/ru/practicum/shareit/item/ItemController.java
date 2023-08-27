package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    public final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemAddDto item) {
        log.info("Запрос на добавление новой вещи: {}", item);
        Item addedItem = itemService.addItem(userId, item);
        log.info("Вещь добавлена: {}", addedItem);
        return addedItem;
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@PathVariable("itemId") Long itemId, @RequestBody @Valid ItemUpdateDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на обновление вещи: {}", item);
        Item updatedItem = itemService.updateItem(itemId, item, userId);
        log.info("Вещь обновлена: {}", updatedItem);
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public ItemGetDto getItem(@PathVariable("itemId") Long itemId) {
        log.info("Запрос на получение вещи с id: {}", itemId);
        ItemGetDto item = itemService.getItem(itemId);
        log.info("Вещь получена: {}", item);
        return item;
    }

    @GetMapping
    public List<ItemGetDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение всех вещей владельца с id: {}", userId);
        List<ItemGetDto> items = itemService.getOwnerItems(userId);
        log.info("Список вещей получен");
        return items;
    }

    @GetMapping("/search")
    public List<ItemGetDto> getSearchedItems(@RequestParam String text) {
        log.info("Запрос на получение вещей по результатам поиска: {}", text);
        List<ItemGetDto> items = itemService.getSearchedItems(text);
        log.info("Список вещей получен");
        return items;
    }

}
