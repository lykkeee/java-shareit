package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemGetDto {
    Long id;
    String name;
    String description;
    Boolean available;
}
