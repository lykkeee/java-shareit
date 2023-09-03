package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    @NotNull
    private LocalDateTime start;
    @Column(name = "end_date")
    @NotNull
    private LocalDateTime end;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "item_id",
            referencedColumnName = "id"
    )
    @NotNull
    private Item item;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "booker_id",
            referencedColumnName = "id"
    )
    @NotNull
    private User booker;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;
}
