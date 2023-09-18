package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getOwnerItems() {
        UserRequestDto user = new UserRequestDto();
        user.setName("name");
        user.setEmail("e@s.com");
        UserResponseDto user1 = userService.createUser(user);


        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item");
        itemRequestDto.setDescription("desc");
        itemRequestDto.setAvailable(true);
        itemService.addItem(user1.getId(), itemRequestDto);

        List<ItemResponseDto> items = itemService.getOwnerItems(user1.getId(), 0, 10);

        assertFalse(items.isEmpty());
        assertEquals("item", items.get(0).getName());
    }
}