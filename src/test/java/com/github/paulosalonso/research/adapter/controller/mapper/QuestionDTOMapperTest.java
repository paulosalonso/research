package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.QuestionCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;
import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QuestionDTOMapperTest {

    @InjectMocks
    private QuestionDTOMapper questionDTOMapper;

    @Mock
    private OptionDTOMapper optionDTOMapper;

    @Test
    public void givenAQuestionWhenMapWithoutOptionsThenReturnDTO() {
        var question = Question.builder()
                .id(UUID.randomUUID())
                .sequence(1)
                .description("description")
                .multiSelect(true)
                .build();

        var dto = questionDTOMapper.toDTO(question, false);

        assertThat(dto.getId()).isEqualTo(question.getId());
        assertThat(dto.getSequence()).isEqualTo(question.getSequence());
        assertThat(dto.getDescription()).isEqualTo(question.getDescription());
        assertThat(dto.getMultiSelect()).isEqualTo(question.getMultiSelect());
    }

    @Test
    public void givenAQuestionWhenMapWithOptionsThenReturnDTO() {
        var option = Option.builder()
                .description("description")
                .build();
        var question = Question.builder()
                .id(UUID.randomUUID())
                .sequence(1)
                .description("description")
                .multiSelect(true)
                .options(Set.of(option))
                .build();

        var dto = questionDTOMapper.toDTO(question, true);

        assertThat(dto.getId()).isEqualTo(question.getId());
        assertThat(dto.getSequence()).isEqualTo(question.getSequence());
        assertThat(dto.getDescription()).isEqualTo(question.getDescription());
        assertThat(dto.getMultiSelect()).isEqualTo(question.getMultiSelect());
        assertThat(dto.getOptions()).hasSize(1);

        verify(optionDTOMapper).toDTO(option);
    }

    @Test
    public void givenAQuestionInputDTOWhenMapThenReturnQuestion() {
        var questionInputDTO = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(true)
                .build();

        var question = questionDTOMapper.toDomain(questionInputDTO);

        assertThat(question.getId()).isNull();
        assertThat(question.getDescription()).isEqualTo(questionInputDTO.getDescription());
        assertThat(question.getMultiSelect()).isEqualTo(questionInputDTO.getMultiSelect());
    }

    @Test
    public void givenAQuestionCriteriaDTOWhenMapThenReturnQuestionCriteria() {
        var questionCriteriaDTO = QuestionCriteriaDTO.builder()
                .description("description")
                .multiSelect(true)
                .build();

        var questionCriteria = questionDTOMapper.toDomain(questionCriteriaDTO);

        assertThat(questionCriteria.getDescription()).isEqualTo(questionCriteriaDTO.getDescription());
        assertThat(questionCriteria.getMultiSelect()).isEqualTo(questionCriteriaDTO.getMultiSelect());
    }
}
