package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
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
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private Long bookerId;
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

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        Booking booking1 = new Booking();
        booking1.setStart(LocalDateTime.now().minusDays(2));
        booking1.setEnd(LocalDateTime.now().minusDays(1));
        booking1.setItem(item);
        booking1.setBooker(user);
        booking1.setStatus(Status.REJECTED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(LocalDateTime.now().plusHours(1));
        booking2.setItem(item);
        booking2.setBooker(user);
        booking2.setStatus(Status.APPROVED);
        bookingRepository.save(booking2);

        this.bookerId = user.getId();
        this.ownerId = owner.getId();
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void findByBookerIdAndEndIsBefore() {
        List<Booking> request = bookingRepository.findByBookerIdAndEndIsBefore(bookerId, PageRequest.of(0, 10)).toList();

        assertFalse(request.isEmpty());
        assertEquals(Status.REJECTED, request.get(0).getStatus());
    }

    @Test
    void findByBookerIdAndStatus() {
        List<Booking> request = bookingRepository.findByBookerIdAndStatus(bookerId, "WAITING", PageRequest.of(0, 10)).toList();

        assertFalse(request.isEmpty());
        assertEquals(Status.WAITING, request.get(0).getStatus());
    }

    @Test
    void findByOwnerIdFuture() {
        List<Booking> request = bookingRepository.findByOwnerIdFuture(ownerId, PageRequest.of(0, 10)).toList();

        assertFalse(request.isEmpty());
        assertEquals(Status.WAITING, request.get(0).getStatus());
    }

    @Test
    void findByOwnerIdCurrent() {
        List<Booking> request = bookingRepository.findByOwnerIdCurrent(ownerId, PageRequest.of(0, 10)).toList();

        assertFalse(request.isEmpty());
        assertEquals(Status.APPROVED, request.get(0).getStatus());
    }
}