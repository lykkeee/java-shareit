package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private User user;

    private User owner;
    private Item item;

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository, new ModelMapper());
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("w@l.com");

        owner = new User();
        owner.setId(2L);
        owner.setName("owner");
        owner.setEmail("w@ld.com");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);
    }

    @Test
    void addItem() {
        ItemRequestDto request = new ItemRequestDto();
        request.setName("item");
        request.setDescription("desc");
        request.setAvailable(true);

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemResponseDto response = itemService.addItem(owner.getId(), request);

        assertEquals("item", response.getName());
    }

    @Test
    void updateItem() {
        ItemUpdateDto request = new ItemUpdateDto();
        request.setName("itemU");

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemResponseDto response = itemService.updateItem(item.getId(), request, owner.getId());

        assertEquals("item", response.getName());
    }

    @Test
    void addComment() {
        CommentRequestDto request = new CommentRequestDto();
        request.setText("comment");

        Comment comment = new Comment();
        comment.setText("comment");
        Booking booking = new Booking();

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findBookingForComment(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto response = itemService.addComment(user.getId(), item.getId(), request);

        assertEquals("comment", response.getText());
    }
}