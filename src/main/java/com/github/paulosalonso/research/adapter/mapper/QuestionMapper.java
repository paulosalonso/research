package com.github.paulosalonso.research.adapter.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.domain.Question;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QuestionMapper {

    public QuestionEntity copy(Question from, QuestionEntity to) {
        to.setDescription(from.getDescription());
        to.setMultiSelect(from.getMultiSelect());

        return to;
    }

    public Question toDomain(QuestionEntity questionEntity) {
        UUID id = questionEntity.getId() != null ? UUID.fromString(questionEntity.getId()) : null;

        return Question.builder()
                .id(id)
                .description(questionEntity.getDescription())
                .multiSelect(questionEntity.getMultiSelect())
                .build();
    }

    public QuestionEntity toEntity(Question question) {
        String id = question.getId() != null ? question.getId().toString() : null;

        return QuestionEntity.builder()
                .id(id)
                .description(question.getDescription())
                .multiSelect(question.getMultiSelect())
                .build();
    }
}
