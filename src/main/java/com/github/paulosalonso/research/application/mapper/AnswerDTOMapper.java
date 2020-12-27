package com.github.paulosalonso.research.application.mapper;

import com.github.paulosalonso.research.application.dto.AnswerCriteriaInputDTO;
import com.github.paulosalonso.research.application.dto.AnswerDTO;
import com.github.paulosalonso.research.application.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
public class AnswerDTOMapper {

    public List<Answer> toDomain(UUID researchId, ResearchAnswerInputDTO dto) {
        return dto.getAnswers().stream()
                .map(answer -> Answer.builder()
                        .researchId(researchId)
                        .questionId(answer.getQuestionId())
                        .optionId(answer.getOptionId())
                        .build())
                .collect(toList());
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
