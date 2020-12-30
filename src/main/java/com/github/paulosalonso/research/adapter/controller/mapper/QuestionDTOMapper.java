package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.QuestionCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;
import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import org.springframework.stereotype.Component;

@Component
public class QuestionDTOMapper {

    public QuestionDTO toDTO(Question question) {
        return QuestionDTO.builder()
                .id(question.getId().toString())
                .sequence(question.getSequence())
                .description(question.getDescription())
                .multiSelect(question.getMultiSelect())
                .build();
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
