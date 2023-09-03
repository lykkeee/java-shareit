package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.IllegalStateException;
import ru.practicum.shareit.exception.IncorrectTimeException;
import ru.practicum.shareit.exception.OwnersBookingException;
import ru.practicum.shareit.exception.UnavailableStatusException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public BookingResponseDto addBooking(Long userId, BookingRequestDto bookingRequestDto) {
        User user = getUser(userId);
        Item item = getItem(bookingRequestDto.getItemId());
        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new OwnersBookingException("Владелец вещи не может её забронировать");
        }
        if (!bookingRequestDto.getEnd().isAfter(bookingRequestDto.getStart())) {
            throw new IncorrectTimeException("Недопустимое время бронирование");
        }
        if (item.getAvailable()) {
            Booking booking = mapper.map(bookingRequestDto, Booking.class);
            booking.setId(null);
            booking.setBooker(user);
            booking.setItem(item);
            booking.setStatus(Status.WAITING);
            return mapper.map(bookingRepository.save(booking), BookingResponseDto.class);
        } else {
            throw new UnavailableStatusException("Вещь недоступна для брони");
        }
    }

    @Override
    public BookingResponseDto approveBooking(Long userId, Long bookingId, Boolean isApproved) {
        Booking booking = getBookingExists(bookingId);
        Item item = booking.getItem();
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new OwnersBookingException("Необходимо являться владельцем вещи, чтобы изменить её статус");
        }
        if (isApproved && booking.getStatus().equals(Status.APPROVED)) {
            throw new UnavailableStatusException("Эта вещь уже имеет статус APPROVED");
        } else if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return mapper.map(bookingRepository.save(booking), BookingResponseDto.class);
    }

    @Override
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        Booking booking = getBookingExists(bookingId);
        if (!(Objects.equals(booking.getBooker().getId(), userId) || Objects.equals(booking.getItem().getOwner().getId(), userId))) {
            throw new OwnersBookingException("Необходимо быть владельцем или иметь бронь на эту вещь");
        }
        return mapper.map(booking, BookingResponseDto.class);
    }

    @Override
    public List<Booking> getUserBookings(Long userId, String state) {
        State enumState;
        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown state: UNSUPPORTED_STATUS");
        }
        getUser(userId);
        switch (enumState) {
            case ALL:
                return bookingRepository.findByBooker_IdOrderByStartDesc(userId);
            case CURRENT:
                return bookingRepository.findByBookerIdCurrent(userId);
            case PAST:
                return bookingRepository.findByBookerIdAndEndIsBefore(userId);
            case FUTURE:
                return bookingRepository.findByBookerIdAndStartIsAfter(userId);
            case WAITING:
                return bookingRepository.findByBookerIdAndStatus(userId, "WAITING");
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatus(userId, "REJECTED");
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public List<Booking> getOwnerBookings(Long userId, String state) {
        State enumState;
        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown state: UNSUPPORTED_STATUS");
        }
        getUser(userId);
        if (itemRepository.findByOwnerId(userId).isEmpty()) {
            throw new OwnersBookingException("Пользователь не является владельцем ни одной вещи");
        }
        switch (enumState) {
            case ALL:
                return bookingRepository.findByOwnerId(userId);
            case CURRENT:
                return bookingRepository.findByOwnerIdCurrent(userId);
            case PAST:
                return bookingRepository.findByOwnerIdPast(userId);
            case FUTURE:
                return bookingRepository.findByOwnerIdFuture(userId);
            case WAITING:
                return bookingRepository.findByOwnerIdStatus(userId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findByOwnerIdStatus(userId, Status.REJECTED);
            default:
                throw new RuntimeException();
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с данным id не найден: " + userId));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Вещь с данным id не найдена: " + itemId));
    }

    private Booking getBookingExists(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Бронирование с данным id не найдено: " + bookingId));
    }

}
