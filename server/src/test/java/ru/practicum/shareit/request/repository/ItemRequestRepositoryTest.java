package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private Long requestorId;

    @BeforeEach
    void beforeEach() {
        User user = new User();
        user.setName("user");
        user.setEmail("a@l.com");
        userRepository.save(user);

        User requestor = new User();
        requestor.setName("requestor");
        requestor.setEmail("a@Ql.com");
        userRepository.save(requestor);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("rdesc");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setDescription("rdesc");
        itemRequest1.setRequestor(requestor);
        itemRequest1.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest1);

        this.requestorId = itemRequest1.getRequestor().getId();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findByNotInRequestorId() {
        List<ItemRequest> request = itemRequestRepository.findByNotInRequestorId(requestorId, PageRequest.of(0, 10)).toList();

        assertFalse(request.isEmpty());
        assertEquals("user", request.get(0).getRequestor().getName());
    }

    @Test
    void findRequestByRequestorIdOrderByCreatedDesc() {
        List<ItemRequest> request = itemRequestRepository.findRequestByRequestorIdOrderByCreatedDesc(requestorId);

        assertFalse(request.isEmpty());
        assertEquals("requestor", request.get(0).getRequestor().getName());
    }
}