package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import java.util.List;

@Data
public class ItemResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingItemDto lastBooking;
    BookingItemDto nextBooking;
    List<CommentResponseDto> comments;
}
