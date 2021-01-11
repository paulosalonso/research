package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.QuestionCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;
import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class QuestionDTOMapper {

    private final OptionDTOMapper optionDTOMapper;

    public QuestionDTO toDTO(Question question, boolean fillOptions) {
        var builder = QuestionDTO.builder()
                .id(question.getId())
                .sequence(question.getSequence())
                .description(question.getDescription())
                .multiSelect(question.getMultiSelect());

        if (fillOptions) {
            builder.options(question.getOptions().stream()
                    .map(optionDTOMapper::toDTO)
                    .collect(toList()));
        }

        return builder.build();
    }

    public Question toDomain(QuestionInputDTO dto) {
        return Question.builder()
                .description(dto.getDescription())
                .multiSelect(dto.getMultiSelect())
                .build();
    }

    public QuestionCriteria toDomain(QuestionCriteriaDTO dto) {
        return QuestionCriteria.builder()
                .description(dto.getDescription())
                .multiSelect(dto.getMultiSelect())
                .build();
    }
}
