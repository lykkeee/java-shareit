package ru.practicum.shareit.item;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.IncorrectUserCommentException;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    @Override
    public Item addItem(Long userId, ItemAddDto itemAddDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с данным id не найден: " + userId));
        Item item = mapper.map(itemAddDto, Item.class);
        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long itemId, ItemUpdateDto itemUpdateDto, Long userId) {
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
        return itemRepository.save(item);
    }

    @Override
    public ItemGetDto getItem(Long itemId, Long userId) {
        Item item = getItemExists(itemId);
        ItemGetDto itemGetDto = mapper.map(item, ItemGetDto.class);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            setBooking(itemGetDto);
        } else {
            itemGetDto.setLastBooking(null);
            itemGetDto.setNextBooking(null);
        }
        setComments(itemGetDto);
        return itemGetDto;
    }

    @Override
    public List<ItemGetDto> getOwnerItems(Long userId) {
        List<ItemGetDto> itemGetDtos = new ArrayList<>();
        for (Item item : itemRepository.findByOwnerId(userId)) {
            ItemGetDto itemGetDto = mapper.map(item, ItemGetDto.class);
            setBooking(itemGetDto);
            setComments(itemGetDto);
            itemGetDtos.add(itemGetDto);
        }
        return itemGetDtos;
    }

    @Override
    public List<ItemGetDto> getSearchedItems(String text) {
        List<ItemGetDto> itemGetDtos = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : itemRepository.search(text)) {
                ItemGetDto itemGetDto = mapper.map(item, ItemGetDto.class);
                setBooking(itemGetDto);
                itemGetDtos.add(itemGetDto);
            }
        }
        return itemGetDtos;
    }

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto text) {
        User user = getUser(userId);
        Item item = getItemExists(itemId);
        if (bookingRepository.findBookingForComment(userId, itemId).isEmpty()) {
            throw new IncorrectUserCommentException("У пользователя нет завершенных бронирований для этого предмета");
        }
        Comment comment = mapper.map(text, Comment.class);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
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

    private void setBooking(ItemGetDto itemGetDto) {
        List<Booking> lastBookings = bookingRepository.findLastBooking(itemGetDto.getId());
        List<Booking> nextBookings = bookingRepository.findNextBooking(itemGetDto.getId());
        if (lastBookings.isEmpty()) {
            itemGetDto.setLastBooking(null);
        } else {
            BookingItemDto bookingItemDto = mapper.map(lastBookings.get(0), BookingItemDto.class);
            bookingItemDto.setBookerId(lastBookings.get(0).getBooker().getId());
            itemGetDto.setLastBooking(bookingItemDto);
        }
        if (nextBookings.isEmpty()) {
            itemGetDto.setNextBooking(null);
        } else {
            BookingItemDto bookingItemDto = mapper.map(nextBookings.get(0), BookingItemDto.class);
            bookingItemDto.setBookerId(nextBookings.get(0).getBooker().getId());
            itemGetDto.setNextBooking(bookingItemDto);
        }
    }

    private void setComments(ItemGetDto itemGetDto) {
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : commentRepository.findByItem_Id(itemGetDto.getId())) {
            String authorName = comment.getAuthor().getName();
            CommentResponseDto commentResponseDto = mapper.map(comment, CommentResponseDto.class);
            commentResponseDto.setAuthorName(authorName);
            commentResponseDtos.add(commentResponseDto);
        }
        itemGetDto.setComments(commentResponseDtos);
    }
}
