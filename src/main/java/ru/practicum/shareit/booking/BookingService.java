package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(Long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto approveBooking(Long userId, Long bookingId, Boolean isApproved);

    BookingResponseDto getBooking(Long userId, Long bookingId);

    List<Booking> getUserBookings(Long userId, String state);

    List<Booking> getOwnerBookings(Long userId, String state);
}
