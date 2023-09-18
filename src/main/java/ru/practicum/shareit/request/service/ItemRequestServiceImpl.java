package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper mapper;

    @Override
    public ItemRequestResponseDto addRequest(ItemRequestRequestDto requestDto, Long userId) {
        User user = getUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.requestToItemRequestModel(requestDto, user);
        return mapper.map(itemRequestRepository.save(itemRequest), ItemRequestResponseDto.class);
    }

    @Override
    public List<ItemRequestItemDto> getRequests(Long userId) {
        getUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findRequestByRequestorIdOrderByCreatedDesc(userId);
        return ItemRequestMapper.requestToRequestItemDtoList(itemRequests, findItemsLists(itemRequests));
    }

    @Override
    public List<ItemRequestItemDto> getAllRequests(Long userId, Integer from, Integer size) {
        getUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByNotInRequestorId(userId, PageRequest.of(from, size)
                .withSort(Sort.by(Sort.Direction.DESC, "created"))).toList();
        return ItemRequestMapper.requestToRequestItemDtoList(itemRequests, findItemsLists(itemRequests));
    }

    @Override
    public ItemRequestItemDto getRequest(Long requestId, Long userId) {
        getUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                        .orElseThrow(() -> new DataNotFoundException("Запрос с данным id не найден: " + requestId));
        List<Item> items = itemRepository.findByRequestId(requestId);
        return ItemRequestMapper.requestToItemRequestItemDto(itemRequest, items);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с данным id не найден: " + userId));
    }

    private List<List<Item>> findItemsLists(List<ItemRequest> itemRequests) {
        List<List<Item>> items = new ArrayList<>();
        if (!itemRequests.isEmpty()) {
            for (ItemRequest itemRequest : itemRequests) {
                items.add(itemRepository.findByRequestId(itemRequest.getId()));
            }
        }
        return items;
    }
}
