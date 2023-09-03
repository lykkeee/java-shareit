package ru.practicum.shareit.comment;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String text;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "item_id",
            referencedColumnName = "id"
    )
    private Item item;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "author_id",
            referencedColumnName = "id"
    )
    private User author;
    private LocalDateTime created;

}