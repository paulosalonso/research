package com.github.paulosalonso.research.adapter.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.AnswerEntity;
import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.Answer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnswerMapper {

    public Answer toDomain(AnswerEntity answerEntity) {
        return Answer.builder()
                .date(answerEntity.getDate())
                .researchId(UUID.fromString(answerEntity.getResearch().getId()))
                .questionId(UUID.fromString(answerEntity.getQuestion().getId()))
                .optionId(UUID.fromString(answerEntity.getOption().getId()))
                .build();
    }

    public AnswerEntity toEntity(Answer answer) {
        return AnswerEntity.builder()
                .date(answer.getDate())
                .research(ResearchEntity.builder().id(answer.getResearchId().toString()).build())
                .question(QuestionEntity.builder().id(answer.getQuestionId().toString()).build())
                .option(OptionEntity.builder().id(answer.getOptionId().toString()).build())
                .build();
    }
}
