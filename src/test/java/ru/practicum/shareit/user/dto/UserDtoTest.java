package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserRequestDto> tester;

    @Test
    @SneakyThrows
    void UserRequestDtoTest() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("name");
        userRequestDto.setEmail("q@e.com");

        JsonContent<UserRequestDto> result = tester.write(userRequestDto);

        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.email");

        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo("q@e.com");
    }

}