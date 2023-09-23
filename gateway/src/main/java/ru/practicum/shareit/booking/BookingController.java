package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        log.info("Запрос на создание нового бронирования от пользователя с id: {}", userId);
        return bookingClient.addBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                 @PathVariable("bookingId") @Positive Long bookingId,
                                                 @RequestParam Boolean approved) {
        log.info("Запрос на подтверждение бронирования с id: {}", bookingId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable("bookingId") @Positive Long bookingId) {
        log.info("Запрос на получение информации о бронирование с id: {}", bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") @Positive  Long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение списка бронирований для пользователя с id: {}", userId);
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") @Positive  Long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение списка бронирований для владельца с id: {}", userId);
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }
}
