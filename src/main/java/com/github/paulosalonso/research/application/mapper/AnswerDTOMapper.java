package com.github.paulosalonso.research.application.mapper;

import com.github.paulosalonso.research.application.dto.AnswerCriteriaInputDTO;
import com.github.paulosalonso.research.application.dto.AnswerDTO;
import com.github.paulosalonso.research.application.dto.AnswerInputDTO;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnswerDTOMapper {

    public Answer toDomain(UUID researchId, AnswerInputDTO dto) {
        return Answer.builder()
                .researchId(researchId)
                .questionId(dto.getQuestionId())
                .optionId(dto.getOptionId())
                .build();
    }

    public AnswerCriteria toDomain(UUID researchId, AnswerCriteriaInputDTO dto) {
        return AnswerCriteria.builder()
                .dateFrom(dto.getDateFrom())
                .dateTo(dto.getDateTo())
                .researchId(researchId)
                .questionId(dto.getQuestionId())
                .build();
    }

    public AnswerDTO toDTO(Answer answer) {
        return AnswerDTO.builder()
                .date(answer.getDate())
                .researchId(answer.getResearchId())
                .questionId(answer.getQuestionId())
                .optionId(answer.getOptionId())
                .build();
    }
}
