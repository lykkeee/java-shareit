package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemGetDto {
    Integer id;
    String name;
    String description;
    Boolean available;
}
