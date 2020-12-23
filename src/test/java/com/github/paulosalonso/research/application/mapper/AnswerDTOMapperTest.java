package com.github.paulosalonso.research.application.mapper;

import com.github.paulosalonso.research.application.dto.AnswerCriteriaInputDTO;
import com.github.paulosalonso.research.application.dto.AnswerInputDTO;
import com.github.paulosalonso.research.domain.Answer;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerDTOMapperTest {

    private AnswerDTOMapper mapper = new AnswerDTOMapper();

    @Test
    public void givenAnAnswerInputDTOWhenMapThenReturnDomain() {
        var researchId = UUID.randomUUID();
        var answerInputDTO = AnswerInputDTO.builder()
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        var answer = mapper.toDomain(researchId, answerInputDTO);

        assertThat(answer.getResearchId()).isEqualTo(researchId);
        assertThat(answer.getQuestionId()).isEqualTo(answerInputDTO.getQuestionId());
        assertThat(answer.getOptionId()).isEqualTo(answerInputDTO.getOptionId());
    }

    @Test
    public void givenAnAnswerCriteriaInputDTOWhenMapThenReturnDomain() {
        var researchId = UUID.randomUUID();
        var answerCriteriaInputDTO = AnswerCriteriaInputDTO.builder()
                .dateFrom(OffsetDateTime.now())
                .dateTo(OffsetDateTime.now().plusMonths(1))
                .questionId(UUID.randomUUID())
                .build();

        var answerCriteria = mapper.toDomain(researchId, answerCriteriaInputDTO);

        assertThat(answerCriteria.getResearchId()).isEqualTo(researchId);
        assertThat(answerCriteria.getDateFrom()).isEqualTo(answerCriteriaInputDTO.getDateFrom());
        assertThat(answerCriteria.getDateTo()).isEqualTo(answerCriteriaInputDTO.getDateTo());
        assertThat(answerCriteria.getQuestionId()).isEqualTo(answerCriteriaInputDTO.getQuestionId());
    }

    @Test
    public void givenAnAnswerWhenMapThenReturnDTO() {
        var answer = Answer.builder()
                .date(OffsetDateTime.now())
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        var answerDTO = mapper.toDTO(answer);

        assertThat(answerDTO.getDate()).isEqualTo(answer.getDate());
        assertThat(answerDTO.getResearchId()).isEqualTo(answer.getResearchId());
        assertThat(answerDTO.getQuestionId()).isEqualTo(answer.getQuestionId());
        assertThat(answerDTO.getOptionId()).isEqualTo(answer.getOptionId());
    }
}
