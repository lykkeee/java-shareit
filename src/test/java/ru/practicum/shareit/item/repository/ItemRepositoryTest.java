package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private Long requestId;
    private Long ownerId;

    @BeforeEach
    void beforeEach() {
        User user = new User();
        user.setName("user");
        user.setEmail("a@l.com");
        userRepository.save(user);

        User owner = new User();
        owner.setName("owner");
        owner.setEmail("a@lQ.com");
        userRepository.save(owner);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("rdesc");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        Item item = new Item();
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        itemRepository.save(item);

        this.requestId = item.getRequest().getId();
        this.ownerId = owner.getId();
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findByOwnerId() {
        List<Item> request = itemRepository.findByOwnerId(ownerId, PageRequest.of(0, 10)).toList();

        assertFalse(request.isEmpty());
        assertEquals("owner", request.get(0).getOwner().getName());
    }

    @Test
    void search() {
        List<Item> request = itemRepository.search("desc", PageRequest.of(0, 10)).toList();

        assertFalse(request.isEmpty());
    }

    @Test
    void findByRequestId() {
        List<Item> request = itemRepository.findByRequestId(requestId);

        assertFalse(request.isEmpty());
        assertEquals("user", request.get(0).getRequest().getRequestor().getName());
    }
}