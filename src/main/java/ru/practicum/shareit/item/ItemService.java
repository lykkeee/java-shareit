package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Long userId, ItemAddDto item);

    Item updateItem(Long itemId, ItemUpdateDto item, Long userId);

    ItemGetDto getItem(Long itemId, Long userId);

    List<ItemGetDto> getOwnerItems(Long userId);

    List<ItemGetDto> getSearchedItems(String text);

    CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto text);
}
