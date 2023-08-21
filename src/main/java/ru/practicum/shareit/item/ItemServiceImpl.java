package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataAlreadyExistsException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private int id = 1;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Item addItem(Integer userId, ItemAddDto itemAddDto) {
        for (User user : userRepository.getUsers()) {
            if (Objects.equals(user.getId(), userId)) {
                Item item = mapper.map(itemAddDto, Item.class);
                item.setId(id++);
                item.setOwner(userRepository.getUser(userId));
                return itemRepository.addItem(item);
            }
        }
        throw new DataNotFoundException("Пользователь с данным id не найден: " + userId);
    }

    @Override
    public Item updateItem(Integer itemId, ItemUpdateDto itemGetDto, Integer userId) {
        for (Item item1 : itemRepository.getItems()) {
            if (Objects.equals(item1.getOwner().getId(), userId)) {
                if (itemGetDto.getAvailable() == null) {
                    itemGetDto.setAvailable(item1.getAvailable());
                }
                if (itemGetDto.getDescription() == null) {
                    itemGetDto.setDescription(item1.getDescription());
                }
                if (itemGetDto.getName() == null) {
                    itemGetDto.setName(item1.getName());
                }
                Item item = mapper.map(itemGetDto, Item.class);
                item.setId(itemId);
                item.setOwner(userRepository.getUser(userId));
                return itemRepository.updateItem(item);
            }
        }
        throw new DataAlreadyExistsException("По данному id владельца вещей не найдено: " + userId);
    }

    @Override
    public ItemGetDto getItem(Integer itemId) {
        Item item = itemRepository.getItem(itemId);
        return mapper.map(item, ItemGetDto.class);
    }

    @Override
    public List<ItemGetDto> getOwnerItems(Integer userId) {
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
