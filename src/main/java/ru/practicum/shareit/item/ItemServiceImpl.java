package ru.practicum.shareit.item;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Item addItem(Long userId, ItemAddDto itemAddDto) {
        if (userRepository.getUser(userId) == null) {
            throw new DataNotFoundException("Пользователь с данным id не найден: " + userId);
        }
        Item item = mapper.map(itemAddDto, Item.class);
        item.setOwner(userRepository.getUser(userId));
        return itemRepository.addItem(item);
    }

    @Override
    public Item updateItem(Long itemId, ItemUpdateDto itemUpdateDto, Long userId) {
        Item item = itemRepository.getItem(itemId);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new DataNotFoundException("Пользователь с id " + userId + " не является владельцем вещи с id " + itemId);
        }
        if (itemUpdateDto.getAvailable() == null) {
            itemUpdateDto.setAvailable(item.getAvailable());
        }
        if (itemUpdateDto.getDescription() == null) {
            itemUpdateDto.setDescription(item.getDescription());
        }
        if (itemUpdateDto.getName() == null) {
            itemUpdateDto.setName(item.getName());
        }
        item = mapper.map(itemUpdateDto, Item.class);
        item.setId(itemId);
        item.setOwner(userRepository.getUser(userId));
        return itemRepository.updateItem(item);
    }

    @Override
    public ItemGetDto getItem(Long itemId) {
        Item item = itemRepository.getItem(itemId);
        return mapper.map(item, ItemGetDto.class);
    }

    @Override
    public List<ItemGetDto> getOwnerItems(Long userId) {
        List<ItemGetDto> itemGetDtos = new ArrayList<>();
        for (Item item : itemRepository.getOwnerItems(userId)) {
            itemGetDtos.add(mapper.map(item, ItemGetDto.class));
        }
        return itemGetDtos;
    }

    @Override
    public List<ItemGetDto> getSearchedItems(String text) {
        List<ItemGetDto> itemGetDtos = new ArrayList<>();
        for (Item item : itemRepository.getSearchedItems(text.toLowerCase())) {
            itemGetDtos.add(mapper.map(item, ItemGetDto.class));
        }
        return itemGetDtos;
    }
}
