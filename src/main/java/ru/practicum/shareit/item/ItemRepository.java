package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();

    public Item addItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Item getItem(Integer itemId) {
        return items.get(itemId);
    }

    public List<Item> getOwnerItems(Integer userId) {
        List<Item> ownerItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (Objects.equals(item.getOwner().getId(), userId)) {
                ownerItems.add(item);
            }
        }
        return ownerItems;
    }

    public List<Item> getSearchedItems(String text) {
        List<Item> searchedItems = new ArrayList<>();
        if (text.isBlank()) {
            return searchedItems;
        }
        for (Item item : items.values()) {
            if (item.getAvailable() && (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))) {
                searchedItems.add(item);
            }
        }
        return searchedItems;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

}
