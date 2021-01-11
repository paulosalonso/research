package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.domain.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Component
public class QuestionMapper {

    private final OptionMapper optionMapper;

    public QuestionEntity copy(Question from, QuestionEntity to) {
        to.setDescription(from.getDescription());
        to.setMultiSelect(from.getMultiSelect());

        return to;
    }

    public Question toDomain(QuestionEntity questionEntity, boolean fillOptions) {
        UUID id = questionEntity.getId() != null ? UUID.fromString(questionEntity.getId()) : null;

        var builder = Question.builder()
                .id(id)
                .sequence(questionEntity.getSequence())
                .description(questionEntity.getDescription())
                .multiSelect(questionEntity.getMultiSelect());

        if (fillOptions) {
            builder.options(questionEntity.getOptions().stream()
                    .map(optionMapper::toDomain)
                    .collect(toSet()));
        }

        return builder.build();
    }

    public QuestionEntity toEntity(Question question) {
        String id = question.getId() != null ? question.getId().toString() : null;

        return QuestionEntity.builder()
                .id(id)
                .sequence(question.getSequence())
                .description(question.getDescription())
                .multiSelect(question.getMultiSelect())
                .build();
    }
}
