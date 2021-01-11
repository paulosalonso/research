package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.domain.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class QuestionMapperTest {

    @InjectMocks
    private QuestionMapper questionMapper;

    @Mock
    private OptionMapper optionMapper;

    @Test
    public void givenAQuestionWhenMapWithoutOptionsThenReturnEntity() {
        var question = Question.builder()
                .id(UUID.randomUUID())
                .description("description")
                .multiSelect(true)
                .build();

        var entity = questionMapper.toEntity(question);

        assertThat(entity.getId()).isEqualTo(question.getId().toString());
        assertThat(entity.getDescription()).isEqualTo(question.getDescription());
        assertThat(entity.getMultiSelect()).isEqualTo(question.getMultiSelect());

        verifyNoInteractions(optionMapper);
    }

    @Test
    public void givenAQuestionWithoutIdWhenMapThenReturnEntity() {
        var question = Question.builder()
                .description("description")
                .multiSelect(true)
                .build();

        var entity = questionMapper.toEntity(question);

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

        var question = questionMapper.toDomain(entity, false);

        assertThat(question.getId()).isEqualTo(UUID.fromString(entity.getId()));
        assertThat(question.getDescription()).isEqualTo(entity.getDescription());
        assertThat(question.getMultiSelect()).isEqualTo(entity.getMultiSelect());

        verifyNoInteractions(optionMapper);
    }

    @Test
    public void givenAQuestionEntityWithoutIdWhenMapWithoutOptionsThenReturnDomain() {
        var entity = QuestionEntity.builder()
                .description("description")
                .multiSelect(true)
                .build();

        var question = questionMapper.toDomain(entity, false);

        assertThat(question.getId()).isNull();
        assertThat(question.getDescription()).isEqualTo(entity.getDescription());
        assertThat(question.getMultiSelect()).isEqualTo(entity.getMultiSelect());

        verifyNoInteractions(optionMapper);
    }

    @Test
    public void givenAQuestionEntityWithoutIdWhenMapWithOptionsThenReturnDomain() {
        var option = OptionEntity.builder().build();
        var entity = QuestionEntity.builder()
                .description("description")
                .multiSelect(true)
                .options(List.of(option))
                .build();

        var question = questionMapper.toDomain(entity, true);

        assertThat(question.getId()).isNull();
        assertThat(question.getDescription()).isEqualTo(entity.getDescription());
        assertThat(question.getMultiSelect()).isEqualTo(entity.getMultiSelect());
        assertThat(question.getOptions()).hasSize(1);

        verify(optionMapper).toDomain(option);
    }

}
