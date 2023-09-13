package ru.practicum.shareit.booking.mapper;

import org.modelmapper.ModelMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking requestToBookingModel(BookingRequestDto bookingRequestDto, Item item, User owner, Status status) {
        ModelMapper  mapper = new ModelMapper();
        Booking booking = mapper.map(bookingRequestDto, Booking.class);
        booking.setId(null);  //Эта строчка здесь потому что маппер автоматически передает в id букинга id вещи
        booking.setItem(item);
        booking.setBooker(owner);
        booking.setStatus(status);
        return booking;
    }
}
