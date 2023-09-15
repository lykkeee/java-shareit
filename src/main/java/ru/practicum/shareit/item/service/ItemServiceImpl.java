package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.IncorrectUserCommentException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;

    @Override
    public ItemResponseDto addItem(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с данным id не найден: " + userId));
        Item item = mapper.map(itemRequestDto, Item.class);
        item.setOwner(user);
        Item addedItem = itemRepository.save(item);
        return mapper.map(addedItem, ItemResponseDto.class);
    }

    @Override
    public ItemResponseDto updateItem(Long itemId, ItemUpdateDto itemUpdateDto, Long userId) {
        User user = getUser(userId);
        Item item = getItemExists(itemId);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new DataNotFoundException("Пользователь с id " + userId + " не является владельцем вещи с id " + itemId);
        }
        if (itemUpdateDto.getAvailable() == null) {
            itemUpdateDto.setAvailable(item.getAvailable());
        }
        if (itemUpdateDto.getDescription() == null) {
            itemUpdateDto.setDescription(item.getDescription());
        }
        if (itemUpdateDto.getName() == null) {
            itemUpdateDto.setName(item.getName());
        }
        item = mapper.map(itemUpdateDto, Item.class);
        item.setId(itemId);
        item.setOwner(user);
        Item updatedItem = itemRepository.save(item);
        return mapper.map(updatedItem, ItemResponseDto.class);
    }

    @Override
    public ItemResponseDto getItem(Long itemId, Long userId) {
        Item item = getItemExists(itemId);
        ItemResponseDto itemResponseDto = mapper.map(item, ItemResponseDto.class);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            setBooking(itemResponseDto);
        } else {
            itemResponseDto.setLastBooking(null);
            itemResponseDto.setNextBooking(null);
        }
        setComments(itemResponseDto);
        return itemResponseDto;
    }

    @Override
    public List<ItemResponseDto> getOwnerItems(Long userId) {
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for (Item item : itemRepository.findByOwnerId(userId)) {
            ItemResponseDto itemResponseDto = mapper.map(item, ItemResponseDto.class);
            setBooking(itemResponseDto);
            setComments(itemResponseDto);
            itemResponseDtos.add(itemResponseDto);
        }
        return itemResponseDtos;
    }

    @Override
    public List<ItemResponseDto> getSearchedItems(String text) {
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : itemRepository.search(text)) {
                ItemResponseDto itemResponseDto = mapper.map(item, ItemResponseDto.class);
                setBooking(itemResponseDto);
                itemResponseDtos.add(itemResponseDto);
            }
        }
        return itemResponseDtos;
    }

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto text) {
        User user = getUser(userId);
        Item item = getItemExists(itemId);
        if (bookingRepository.findBookingForComment(userId, itemId).isEmpty()) {
            throw new IncorrectUserCommentException("У пользователя нет завершенных бронирований для этого предмета");
        }
        Comment comment = CommentMapper.requestToCommentModel(text, user, item);
        CommentResponseDto commentResponseDto = mapper.map(commentRepository.save(comment), CommentResponseDto.class);
        commentResponseDto.setAuthorName(user.getName());
        return commentResponseDto;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с данным id не найден: " + userId));
    }

    private Item getItemExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Вещь с данным id не найдена: " + itemId));
    }

    private void setBooking(ItemResponseDto itemResponseDto) {
        List<Booking> lastBookings = bookingRepository.findLastBooking(itemResponseDto.getId());
        List<Booking> nextBookings = bookingRepository.findNextBooking(itemResponseDto.getId());
        if (lastBookings.isEmpty()) {
            itemResponseDto.setLastBooking(null);
        } else {
            BookingItemDto bookingItemDto = mapper.map(lastBookings.get(0), BookingItemDto.class);
            bookingItemDto.setBookerId(lastBookings.get(0).getBooker().getId());
            itemResponseDto.setLastBooking(bookingItemDto);
        }
        if (nextBookings.isEmpty()) {
            itemResponseDto.setNextBooking(null);
        } else {
            BookingItemDto bookingItemDto = mapper.map(nextBookings.get(0), BookingItemDto.class);
            bookingItemDto.setBookerId(nextBookings.get(0).getBooker().getId());
            itemResponseDto.setNextBooking(bookingItemDto);
        }
    }

    private void setComments(ItemResponseDto itemResponseDto) {
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : commentRepository.findByItemId(itemResponseDto.getId())) {
            String authorName = comment.getAuthor().getName();
            CommentResponseDto commentResponseDto = mapper.map(comment, CommentResponseDto.class);
            commentResponseDto.setAuthorName(authorName);
            commentResponseDtos.add(commentResponseDto);
        }
        itemResponseDto.setComments(commentResponseDtos);
    }
}
