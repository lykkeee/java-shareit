package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private User owner;
    private Booking booking;
    private Item item;

    @BeforeEach
    void beforeEach() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository, new ModelMapper());
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

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
    }


    @Test
    void addBooking() {
        BookingRequestDto request = new BookingRequestDto();
        request.setItemId(1L);
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusDays(1));

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto response = bookingService.addBooking(user.getId(), request);

        assertEquals("item", response.getItem().getName());
    }

    @Test
    void approveBooking() {
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto response = bookingService.approveBooking(owner.getId(), booking.getId(), true);

        assertEquals(Status.APPROVED, response.getStatus());
    }

    @Test
    void getBooking() {
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));

        BookingResponseDto response = bookingService.getBooking(user.getId(), booking.getId());

        assertEquals("name", response.getBooker().getName());
    }
}