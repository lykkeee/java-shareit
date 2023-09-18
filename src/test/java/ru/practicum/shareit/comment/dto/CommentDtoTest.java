package ru.practicum.shareit.comment.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentRequestDto> tester;

    @Test
    @SneakyThrows
    void commentRequestDtoTest() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("text");

        JsonContent<CommentRequestDto> result = tester.write(commentRequestDto);

        assertThat(result).hasJsonPath("$.text");
        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("text");
    }

}