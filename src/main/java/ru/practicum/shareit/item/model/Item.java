package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Column(name = "is_available")
    private Boolean available;
    @NotNull
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "owner_id",
            referencedColumnName = "id"
    )
    private User owner;
/*    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "request_id",
            referencedColumnName = "id"
    )
    private ItemRequest request;*/
}
