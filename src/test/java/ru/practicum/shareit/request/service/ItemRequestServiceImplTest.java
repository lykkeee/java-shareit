package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private ItemRequest itemRequest;


    @BeforeEach
    void beforeEach() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRepository, new ModelMapper());
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("w@l.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("desc");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
    }

    @Test
    void addRequest() {
        ItemRequestRequestDto request = new ItemRequestRequestDto();
        request.setDescription("desc");

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestResponseDto response = itemRequestService.addRequest(request, user.getId());

        assertEquals(1L, response.getId());
        assertEquals("desc", response.getDescription());
    }

    @Test
    void getRequests() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(Collections.singletonList(item));
        when(itemRequestRepository.findRequestByRequestorIdOrderByCreatedDesc(Mockito.anyLong())).thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestItemDto> response = itemRequestService.getRequests(user.getId());

        assertFalse(response.isEmpty());
        assertEquals("desc", response.get(0).getDescription());

    }

    @Test
    void getAllRequests() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(Collections.singletonList(item));
        when(itemRequestRepository.findByNotInRequestorId(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(itemRequest)));

        List<ItemRequestItemDto> response = itemRequestService.getAllRequests(user.getId(), 0, 10);

        assertFalse(response.isEmpty());
        assertEquals("desc", response.get(0).getDescription());
    }

    @Test
    void getRequest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(Collections.singletonList(item));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestItemDto itemRequestItemDto = itemRequestService.getRequest(itemRequest.getId(), user.getId());

        assertEquals("desc", itemRequestItemDto.getDescription());
    }
}