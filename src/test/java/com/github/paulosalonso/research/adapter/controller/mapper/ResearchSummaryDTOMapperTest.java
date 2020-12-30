package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.AnswerCriteriaInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO.QuestionAnswerInputDTO;
import com.github.paulosalonso.research.domain.ResearchSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.OptionSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.QuestionSummary;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ResearchSummaryDTOMapperTest {

    private AnswerDTOMapper mapper = new AnswerDTOMapper();

    @Test
    public void givenAnAnswerInputDTOWhenMapThenReturnDomain() {
        var researchId = UUID.randomUUID();
        var answerInputDTO = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(UUID.randomUUID())
                        .optionId(UUID.randomUUID())
                        .build())
                .build();

        var result = mapper.toDomain(researchId, answerInputDTO);

        assertThat(result)
                .hasSize(1)
                .first()
                .satisfies(answer -> {
                    assertThat(answer.getResearchId()).isEqualTo(researchId);
                    assertThat(answer.getQuestionId()).isEqualTo(answerInputDTO.getAnswers().get(0).getQuestionId());
                    assertThat(answer.getOptionId()).isEqualTo(answerInputDTO.getAnswers().get(0).getOptionId());
                });
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
    public void givenAResearchSummaryWhenMapThenReturnDTO() {
        var researchSummary = ResearchSummary.builder()
                .id(UUID.randomUUID())
                .title("title")
                .questions(List.of(QuestionSummary.builder()
                        .id(UUID.randomUUID())
                        .description("description")
                        .options(List.of(OptionSummary.builder()
                                .id(UUID.randomUUID())
                                .description("description")
                                .amount(10L)
                                .build()))
                        .build()))
                .build();

        var answerCriteriaInputDTO = AnswerCriteriaInputDTO.builder()
                .questionId(UUID.randomUUID())
                .dateFrom(OffsetDateTime.now())
                .dateTo(OffsetDateTime.now().plusMonths(1))
                .build();

        var researchSummaryDTO = mapper.toDTO(researchSummary, answerCriteriaInputDTO);

        assertThat(researchSummaryDTO.getId()).isEqualTo(researchSummary.getId());
        assertThat(researchSummaryDTO.getTitle()).isEqualTo(researchSummary.getTitle());
        assertThat(researchSummaryDTO.getCriteria()).isSameAs(answerCriteriaInputDTO);
        assertThat(researchSummaryDTO.getQuestions())
                .hasSize(1)
                .first()
                .satisfies(question -> {
                    assertThat(question.getId()).isEqualTo(researchSummary.getQuestions().get(0).getId());
                    assertThat(question.getDescription()).isEqualTo(researchSummary.getQuestions().get(0).getDescription());
                    assertThat(question.getOptions())
                            .hasSize(1)
                            .first()
                            .satisfies(option -> {
                                assertThat(option.getId()).isEqualTo(researchSummary.getQuestions().get(0).getOptions().get(0).getId());
                                assertThat(option.getDescription()).isEqualTo(researchSummary.getQuestions().get(0).getOptions().get(0).getDescription());
                                assertThat(option.getAmount()).isEqualTo(researchSummary.getQuestions().get(0).getOptions().get(0).getAmount());
                            });
                });
    }
}
