package com.github.paulosalonso.research.adapter.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.*;
import com.github.paulosalonso.research.domain.Answer;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerMapperTest {

    private AnswerMapper mapper = new AnswerMapper();

    @Test
    public void givenAnAnswerEntityWhenMapThenReturnDomain() {
        var date = OffsetDateTime.now();

        var entity = AnswerEntity.builder()
                .date(date)
                .research(ResearchEntity.builder().id(UUID.randomUUID().toString()).build())
                .question(QuestionEntity.builder().id(UUID.randomUUID().toString()).build())
                .option(OptionEntity.builder().id(UUID.randomUUID().toString()).build())
                .build();

        var answer = mapper.toDomain(entity);

        assertThat(answer.getDate()).isEqualTo(date);
        assertThat(answer.getResearchId()).isEqualTo(UUID.fromString(entity.getResearch().getId()));
        assertThat(answer.getQuestionId()).isEqualTo(UUID.fromString(entity.getQuestion().getId()));
        assertThat(answer.getOptionId()).isEqualTo(UUID.fromString(entity.getOption().getId()));
    }

    @Test
    public void givenAnAnswerWhenMapThenReturnAnswerEntity() {
        var date = OffsetDateTime.now();

        var answer = Answer.builder()
                .date(date)
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        var entity = mapper.toEntity(answer);

        assertThat(entity.getDate()).isEqualTo(date);
        assertThat(entity.getResearch().getId()).isEqualTo(answer.getResearchId().toString());
        assertThat(entity.getQuestion().getId()).isEqualTo(answer.getQuestionId().toString());
        assertThat(entity.getOption().getId()).isEqualTo(answer.getOptionId().toString());
    }

    @Test
    public void givenAResearchSummaryModelListWhenMapThenReturnResearchSummary() {
        var research = ResearchEntity.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .build();

        var questionA = buildQuestion(UUID.randomUUID());
        var optionAA = buildOption(UUID.randomUUID(), 1);
        var optionAB = buildOption(UUID.randomUUID(), 2);
        questionA.setOptions(List.of(optionAA, optionAB));

        var questionB = buildQuestion(UUID.randomUUID());
        var optionBA = buildOption(UUID.randomUUID(), 1);
        var optionBB = buildOption(UUID.randomUUID(), 2);
        questionB.setOptions(List.of(optionBA, optionBB));

        research.setQuestions(List.of(questionA, questionB));

        var summaryModel = List.of(
                ResearchSummaryModel.builder()
                        .question(questionA)
                        .option(optionAA)
                        .amount(5L)
                        .build(),
                ResearchSummaryModel.builder()
                        .question(questionA)
                        .option(optionAB)
                        .amount(10L)
                        .build(),
                ResearchSummaryModel.builder()
                        .question(questionB)
                        .option(optionBA)
                        .amount(7L)
                        .build(),
                ResearchSummaryModel.builder()
                        .question(questionB)
                        .option(optionBB)
                        .amount(14L)
                        .build());

        var summary = mapper.toDomain(research, summaryModel);

        assertThat(summary.getId()).isEqualTo(UUID.fromString(research.getId()));
        assertThat(summary.getTitle()).isEqualTo(research.getTitle());
        assertThat(summary.getQuestions()).hasSize(2)
                .satisfies(questions -> {
                    var mappedQuestionA = questions.get(0);
                    assertThat(mappedQuestionA.getId()).isEqualTo(UUID.fromString(questionA.getId()));
                    assertThat(mappedQuestionA.getSequence()).isEqualTo(questionA.getSequence());
                    assertThat(mappedQuestionA.getDescription()).isEqualTo(questionA.getDescription());
                    assertThat(mappedQuestionA.getOptions())
                            .hasSize(2)
                            .satisfies(options -> {
                                var mappedOptionAA = options.get(0);
                                assertThat(mappedOptionAA.getId()).isEqualTo(UUID.fromString(optionAA.getId()));
                                assertThat(mappedOptionAA.getSequence()).isEqualTo(optionAA.getSequence());
                                assertThat(mappedOptionAA.getDescription()).isEqualTo(optionAA.getDescription());
                                assertThat(mappedOptionAA.getAmount()).isEqualTo(5L);

                                var mappedOptionAB = options.get(1);
                                assertThat(mappedOptionAB.getId()).isEqualTo(UUID.fromString(optionAB.getId()));
                                assertThat(mappedOptionAB.getSequence()).isEqualTo(optionAB.getSequence());
                                assertThat(mappedOptionAB.getDescription()).isEqualTo(optionAB.getDescription());
                                assertThat(mappedOptionAB.getAmount()).isEqualTo(10L);
                            });

                    var mappedQuestionB = questions.get(1);
                    assertThat(mappedQuestionB.getId()).isEqualTo(UUID.fromString(questionB.getId()));
                    assertThat(mappedQuestionB.getSequence()).isEqualTo(questionB.getSequence());
                    assertThat(mappedQuestionB.getDescription()).isEqualTo(questionB.getDescription());
                    assertThat(mappedQuestionB.getOptions())
                            .hasSize(2)
                            .satisfies(options -> {
                                var mappedOptionBA = options.get(0);
                                assertThat(mappedOptionBA.getId()).isEqualTo(UUID.fromString(optionBA.getId()));
                                assertThat(mappedOptionBA.getSequence()).isEqualTo(optionBA.getSequence());
                                assertThat(mappedOptionBA.getDescription()).isEqualTo(optionBA.getDescription());
                                assertThat(mappedOptionBA.getAmount()).isEqualTo(7L);

                                var mappedOptionBB = options.get(1);
                                assertThat(mappedOptionBB.getId()).isEqualTo(UUID.fromString(optionBB.getId()));
                                assertThat(mappedOptionBB.getSequence()).isEqualTo(optionBB.getSequence());
                                assertThat(mappedOptionBB.getDescription()).isEqualTo(optionBB.getDescription());
                                assertThat(mappedOptionBB.getAmount()).isEqualTo(14L);
                            });
                });
    }

    private QuestionEntity buildQuestion(UUID id) {
        return QuestionEntity.builder()
                .id(id.toString())
                .description("description " + id)
                .build();
    }

    private OptionEntity buildOption(UUID id, Integer sequence) {
        return OptionEntity.builder()
                .id(id.toString())
                .sequence(sequence)
                .description("description " + id)
                .build();
    }
}
