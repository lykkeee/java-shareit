package ru.practicum.shareit.comment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Test
    void findByItemId() {
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

        Comment comment = new Comment();
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        List<Comment> request = commentRepository.findByItemId(item.getId());

        assertFalse(request.isEmpty());
        assertEquals("user", request.get(0).getAuthor().getName());
    }
}