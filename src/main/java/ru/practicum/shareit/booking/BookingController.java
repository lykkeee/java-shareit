package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        log.info("Запрос на создание нового бронирования от пользователя с id: {}", userId);
        BookingResponseDto booking = bookingService.addBooking(userId, bookingRequestDto);
        log.info("Запрос на бронирование выполнен");
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("Запрос на подтверждение бронирования с id: {}", bookingId);
        BookingResponseDto booking = bookingService.approveBooking(userId, bookingId, approved);
        log.info("Запрос выполнен");
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("Запрос на получение информации о бронирование с id: {}", bookingId);
        BookingResponseDto booking = bookingService.getBooking(userId, bookingId);
        log.info("Запрос на получение информации выполнен");
        return booking;
    }

    @GetMapping()
    public List<Booking> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("Запрос на получение списка бронирований для пользователя с id: {}", userId);
        List<Booking> bookings = bookingService.getUserBookings(userId, state);
        log.info("Список бронирований получен");
        return bookings;
    }

    @GetMapping("/owner")
    public List<Booking> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("Запрос на получение списка бронирований для владельца с id: {}", userId);
        List<Booking> bookings = bookingService.getOwnerBookings(userId, state);
        log.info("Список бронирований получен");
        return bookings;
    }
}
