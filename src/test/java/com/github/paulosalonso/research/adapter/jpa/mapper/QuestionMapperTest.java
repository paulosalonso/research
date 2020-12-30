package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.domain.Question;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionMapperTest {

    private final QuestionMapper mapper = new QuestionMapper();

    @Test
    public void givenAQuestionWhenMapThenReturnEntity() {
        var question = Question.builder()
                .id(UUID.randomUUID())
                .description("description")
                .multiSelect(true)
                .build();

        var entity = mapper.toEntity(question);

        assertThat(entity.getId()).isEqualTo(question.getId().toString());
        assertThat(entity.getDescription()).isEqualTo(question.getDescription());
        assertThat(entity.getMultiSelect()).isEqualTo(question.getMultiSelect());
    }

    @Test
    public void givenAQuestionWithoutIdWhenMapThenReturnEntity() {
        var question = Question.builder()
                .description("description")
                .multiSelect(true)
                .build();

        var entity = mapper.toEntity(question);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getDescription()).isEqualTo(question.getDescription());
        assertThat(entity.getMultiSelect()).isEqualTo(question.getMultiSelect());
    }

    @Test
    public void givenAQuestionEntityWhenMapThenReturnDomain() {
        var entity = QuestionEntity.builder()
                .id(UUID.randomUUID().toString())
                .description("description")
                .multiSelect(true)
                .build();

        var question = mapper.toDomain(entity);

        assertThat(question.getId()).isEqualTo(UUID.fromString(entity.getId()));
        assertThat(question.getDescription()).isEqualTo(entity.getDescription());
        assertThat(question.getMultiSelect()).isEqualTo(entity.getMultiSelect());
    }

    @Test
    public void givenAQuestionEntityWithoutIdWhenMapThenReturnDomain() {
        var entity = QuestionEntity.builder()
                .description("description")
                .multiSelect(true)
                .build();

        var question = mapper.toDomain(entity);

        assertThat(question.getId()).isNull();
        assertThat(question.getDescription()).isEqualTo(entity.getDescription());
        assertThat(question.getMultiSelect()).isEqualTo(entity.getMultiSelect());
    }

}
