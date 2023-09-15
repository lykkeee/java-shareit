package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(Long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto approveBooking(Long userId, Long bookingId, Boolean isApproved);

    BookingResponseDto getBooking(Long userId, Long bookingId);

    List<BookingResponseDto> getUserBookings(Long userId, String state);

    List<BookingResponseDto> getOwnerBookings(Long userId, String state);
}
