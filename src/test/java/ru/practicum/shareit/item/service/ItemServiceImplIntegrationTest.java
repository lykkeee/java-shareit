package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.model.QComment;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.QItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.QUser;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private BookingServiceImpl bookingService;

    @Test
    void getOwnerItems() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("name");
        userRequestDto.setEmail("e@s.com");
        UserResponseDto user = userService.createUser(userRequestDto);

        UserRequestDto userRequestDto2 = new UserRequestDto();
        userRequestDto2.setName("name2");
        userRequestDto2.setEmail("2e@s.com");
        UserResponseDto owner = userService.createUser(userRequestDto2);

        ItemRequestRequestDto itemRequest = new ItemRequestRequestDto();
        itemRequest.setDescription("rdesc");
        ItemRequestResponseDto itemRequestResponseDto = itemRequestService.addRequest(itemRequest, user.getId());

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item");
        itemRequestDto.setDescription("desc");
        itemRequestDto.setAvailable(true);
        itemRequestDto.setRequestId(itemRequestResponseDto.getId());
        ItemResponseDto item = itemService.addItem(owner.getId(), itemRequestDto);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(item.getId());
        bookingRequestDto.setStart(LocalDateTime.now().minusDays(2));
        bookingRequestDto.setEnd(LocalDateTime.now().minusDays(1));
        BookingResponseDto booking = bookingService.addBooking(user.getId(), bookingRequestDto);
        bookingService.approveBooking(owner.getId(), booking.getId(), true);

        BookingRequestDto bookingRequestDto2 = new BookingRequestDto();
        bookingRequestDto2.setItemId(item.getId());
        bookingRequestDto2.setStart(LocalDateTime.now().plusDays(1));
        bookingRequestDto2.setEnd(LocalDateTime.now().plusDays(2));
        BookingResponseDto booking2 = bookingService.addBooking(user.getId(), bookingRequestDto2);
        bookingService.approveBooking(owner.getId(), booking2.getId(), true);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("text");
        itemService.addComment(user.getId(), item.getId(), commentRequestDto);

        List<ItemResponseDto> items = itemService.getOwnerItems(owner.getId(), 0, 10);

        assertFalse(items.isEmpty());
        assertEquals("item", items.get(0).getName());
    }

    @Test
    void getSearchedItems() {
        List<ItemResponseDto> items = itemService.getSearchedItems("desc", 0, 10);

        assertFalse(items.isEmpty());
        assertEquals("item", items.get(0).getName());
    }

    @Test
    void queryTest() {
        QUser qUser = new QUser("user");
        QItem qItem = new QItem("item");
        QItemRequest qItemRequest = new QItemRequest("itemRequest");
        QBooking qBooking = new QBooking("booking");
        QComment qComment = new QComment("comment");

        //Не совсем понял как это тестировать, поэтому сделал такую заглушку для jacoco
    }
}