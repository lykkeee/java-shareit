package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Integer userId, ItemAddDto item);

    Item updateItem(Integer itemId, ItemUpdateDto item, Integer userId);

    ItemGetDto getItem(Integer itemId);

    List<ItemGetDto> getOwnerItems(Integer userId);

    List<ItemGetDto> getSearchedItems(String text);
}
