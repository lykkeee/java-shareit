package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.IllegalStateException;
import ru.practicum.shareit.exception.IncorrectTimeException;
import ru.practicum.shareit.exception.OwnersBookingException;
import ru.practicum.shareit.exception.UnavailableStatusException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            Booking booking = BookingMapper.requestToBookingModel(bookingRequestDto, item, user, Status.WAITING);
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
    public List<BookingResponseDto> getUserBookings(Long userId, String state) {
        State enumState;
        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown state: UNSUPPORTED_STATUS");
        }
        getUser(userId);
        List<Booking> response = new ArrayList<>();
        switch (enumState) {
            case ALL:
                response = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
                break;
            case CURRENT:
                response = bookingRepository.findByBookerIdCurrent(userId);
                break;
            case PAST:
                response = bookingRepository.findByBookerIdAndEndIsBefore(userId);
                break;
            case FUTURE:
                response = bookingRepository.findByBookerIdAndStartIsAfter(userId);
                break;
            case WAITING:
                response = bookingRepository.findByBookerIdAndStatus(userId, "WAITING");
                break;
            case REJECTED:
                response = bookingRepository.findByBookerIdAndStatus(userId, "REJECTED");
                break;
        }
        return response.stream().map(booking -> mapper.map(booking, BookingResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Long userId, String state) {
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
        List<Booking> response = new ArrayList<>();
        switch (enumState) {
            case ALL:
                response = bookingRepository.findByOwnerId(userId);
                break;
            case CURRENT:
                response = bookingRepository.findByOwnerIdCurrent(userId);
                break;
            case PAST:
                response = bookingRepository.findByOwnerIdPast(userId);
                break;
            case FUTURE:
                response = bookingRepository.findByOwnerIdFuture(userId);
                break;
            case WAITING:
                response = bookingRepository.findByOwnerIdStatus(userId, Status.WAITING);
                break;
            case REJECTED:
                response = bookingRepository.findByOwnerIdStatus(userId, Status.REJECTED);
                break;
        }
        return response.stream().map(booking -> mapper.map(booking, BookingResponseDto.class)).collect(Collectors.toList());
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
