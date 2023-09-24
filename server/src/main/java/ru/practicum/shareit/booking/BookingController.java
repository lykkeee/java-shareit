package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingRequestDto bookingRequestDto) {
        BookingResponseDto booking = bookingService.addBooking(userId, bookingRequestDto);
        log.info("Запрос на бронирование выполнен");
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        BookingResponseDto booking = bookingService.approveBooking(userId, bookingId, approved);
        log.info("Запрос выполнен");
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        BookingResponseDto booking = bookingService.getBooking(userId, bookingId);
        log.info("Запрос на получение информации выполнен");
        return booking;
    }

    @GetMapping()
    public List<BookingResponseDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam String state,
                                                    @RequestParam Integer from,
                                                    @RequestParam Integer size) {
        List<BookingResponseDto> bookings = bookingService.getUserBookings(userId, state, from, size);
        log.info("Список бронирований получен");
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam String state,
                                                     @RequestParam Integer from,
                                                     @RequestParam Integer size) {
        List<BookingResponseDto> bookings = bookingService.getOwnerBookings(userId, state, from, size);
        log.info("Список бронирований получен");
        return bookings;
    }

}
