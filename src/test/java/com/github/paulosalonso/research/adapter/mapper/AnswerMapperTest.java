package com.github.paulosalonso.research.adapter.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.AnswerEntity;
import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.Answer;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
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
}
