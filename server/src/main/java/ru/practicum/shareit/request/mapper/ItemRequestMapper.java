package ru.practicum.shareit.request.mapper;

import org.modelmapper.ModelMapper;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest requestToItemRequestModel(ItemRequestRequestDto requestDto, User user) {
        ItemRequest request = new ItemRequest();
        request.setDescription(requestDto.getDescription());
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public static ItemRequestItemDto requestToItemRequestItemDto(ItemRequest itemRequest, List<Item> items) {
        ModelMapper mapper = new ModelMapper();
        ItemRequestItemDto itemRequestItemDto = mapper.map(itemRequest, ItemRequestItemDto.class);
        List<ItemForRequestDto> itemForRequestDtoList = items.stream()
                .map(item -> mapper.map(item, ItemForRequestDto.class)).collect(Collectors.toList());
        itemRequestItemDto.setItems(itemForRequestDtoList);
        return itemRequestItemDto;
    }

    public static List<ItemRequestItemDto> requestToRequestItemDtoList(List<ItemRequest> itemRequests, List<List<Item>> items) {
        ModelMapper mapper = new ModelMapper();
        List<ItemRequestItemDto> itemRequestItemDtoList = new ArrayList<>();
        for (int i = 0; i < itemRequests.size(); i++) {
            ItemRequestItemDto itemRequestItemDto = mapper.map(itemRequests.get(i), ItemRequestItemDto.class);
            List<Item> items1 = items.get(i);
            List<ItemForRequestDto> itemForRequestDtoList = items1.stream()
                    .map(item -> mapper.map(item, ItemForRequestDto.class)).collect(Collectors.toList());
            itemRequestItemDto.setItems(itemForRequestDtoList);
            itemRequestItemDtoList.add(itemRequestItemDto);
        }
        return itemRequestItemDtoList;
    }
}
