package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> tester;

    @Test
    @SneakyThrows
    void itemRequestDtoTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("name");
        itemRequestDto.setDescription("desc");
        itemRequestDto.setAvailable(true);

        JsonContent<ItemRequestDto> result = tester.write(itemRequestDto);

        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");

        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("desc");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
    }

}