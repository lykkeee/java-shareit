package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IllegalStateException;
import ru.practicum.shareit.exception.IncorrectTimeException;
import ru.practicum.shareit.exception.OwnersBookingException;
import ru.practicum.shareit.exception.UnavailableStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        request.setEnd(LocalDateTime.now().minusDays(1));

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        assertThrows(IncorrectTimeException.class, () -> {
            BookingResponseDto response = bookingService.addBooking(user.getId(), request);
        });

        request.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(OwnersBookingException.class, () -> {
            BookingResponseDto response = bookingService.addBooking(owner.getId(), request);
        });

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));

        item.setAvailable(false);
        assertThrows(UnavailableStatusException.class, () -> {
            BookingResponseDto response = bookingService.addBooking(user.getId(), request);
        });

        item.setAvailable(true);
        BookingResponseDto response = bookingService.addBooking(user.getId(), request);

        assertEquals("item", response.getItem().getName());
    }

    @Test
    void approveBooking() {
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto response = bookingService.approveBooking(owner.getId(), booking.getId(), true);
        assertEquals(Status.APPROVED, response.getStatus());

        response = bookingService.approveBooking(owner.getId(), booking.getId(), false);
        assertEquals(Status.REJECTED, response.getStatus());

        booking.setStatus(Status.APPROVED);
        assertThrows(UnavailableStatusException.class, () -> {
            BookingResponseDto response1 = bookingService.approveBooking(owner.getId(), booking.getId(), true);
        });

        assertThrows(OwnersBookingException.class, () -> {
            BookingResponseDto response1 = bookingService.approveBooking(user.getId(), booking.getId(), true);
        });
    }

    @Test
    void getBooking() {
        User guest = new User();
        guest.setId(3L);

        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));

        BookingResponseDto response = bookingService.getBooking(user.getId(), booking.getId());

        assertEquals("name", response.getBooker().getName());

        assertThrows(OwnersBookingException.class, () -> {
            BookingResponseDto response1 = bookingService.getBooking(guest.getId(), booking.getId());
        });
    }

    @Test
    void getUserBookings() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findByBooker_IdOrderByStartDesc(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), State.ALL.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getUserBookingsCurrent() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findByBookerIdCurrent(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), State.CURRENT.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getUserBookingsPast() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findByBookerIdAndEndIsBefore(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), State.PAST.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getUserBookingsFuture() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findByBookerIdAndStartIsAfter(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), State.FUTURE.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getUserBookingsWaiting() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findByBookerIdAndStatus(Mockito.anyLong(), anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), State.WAITING.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getUserBookingsRejected() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findByBookerIdAndStatus(Mockito.anyLong(), anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), State.REJECTED.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getUserBookingsFail() {
        assertThrows(IllegalStateException.class, () -> {
            List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), "FAIl", 0, 10);
        });

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findByBookerIdAndStartIsAfter(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), State.FUTURE.toString(), 11, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getOwnerBookingsAll() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(item)));
        when(bookingRepository.findByOwnerId(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getOwnerBookings(owner.getId(), State.ALL.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getOwnerBookingsCurrent() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(item)));
        when(bookingRepository.findByOwnerIdCurrent(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getOwnerBookings(owner.getId(), State.CURRENT.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getOwnerBookingsPast() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(item)));
        when(bookingRepository.findByOwnerIdPast(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getOwnerBookings(owner.getId(), State.PAST.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getOwnerBookingsFuture() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(item)));
        when(bookingRepository.findByOwnerIdFuture(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getOwnerBookings(owner.getId(), State.FUTURE.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getOwnerBookingsWaiting() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(item)));
        when(bookingRepository.findByOwnerIdStatus(Mockito.anyLong(), any(Status.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getOwnerBookings(owner.getId(), State.WAITING.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getOwnerBookingsRejected() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(item)));
        when(bookingRepository.findByOwnerIdStatus(Mockito.anyLong(), any(Status.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> response = bookingService.getOwnerBookings(owner.getId(), State.REJECTED.toString(), 0, 10);
        assertFalse(response.isEmpty());
    }

    @Test
    void getOwnerBookingsFail() {
        assertThrows(IllegalStateException.class, () -> {
            List<BookingResponseDto> response = bookingService.getOwnerBookings(owner.getId(), "FAIl", 0, 10);
        });

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        assertThrows(OwnersBookingException.class, () -> {
            List<BookingResponseDto> response = bookingService.getOwnerBookings(user.getId(), "ALL", 0, 10);
        });
    }
}