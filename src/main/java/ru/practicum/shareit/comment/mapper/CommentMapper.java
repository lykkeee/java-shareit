package ru.practicum.shareit.comment.mapper;

import org.modelmapper.ModelMapper;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment requestToCommentModel(CommentRequestDto text, User author, Item item) {
        ModelMapper mapper = new ModelMapper();
        Comment comment = mapper.map(text, Comment.class);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}
