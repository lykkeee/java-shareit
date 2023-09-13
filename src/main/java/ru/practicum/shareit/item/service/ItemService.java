package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemResponseDto addItem(Long userId, ItemRequestDto item);

    ItemResponseDto updateItem(Long itemId, ItemUpdateDto item, Long userId);

    ItemResponseDto getItem(Long itemId, Long userId);

    List<ItemResponseDto> getOwnerItems(Long userId);

    List<ItemResponseDto> getSearchedItems(String text);

    CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto text);
}
