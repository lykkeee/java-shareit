package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestResponseDto addRequest(ItemRequestRequestDto requestDto, Long userId);

    List<ItemRequestItemDto> getRequests(Long userId);

    List<ItemRequestItemDto> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestItemDto getRequest(Long requestId, Long userId);
}
