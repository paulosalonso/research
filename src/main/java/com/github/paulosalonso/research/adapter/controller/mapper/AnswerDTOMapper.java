package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.AnswerCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchSummaryDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchSummaryDTO.OptionSummaryDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchSummaryDTO.QuestionSummaryDTO;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.domain.ResearchSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.OptionSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.QuestionSummary;
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

    public AnswerCriteria toDomain(UUID researchId, AnswerCriteriaDTO dto) {
        return AnswerCriteria.builder()
                .dateFrom(dto.getDateFrom())
                .dateTo(dto.getDateTo())
                .researchId(researchId)
                .questionId(dto.getQuestionId())
                .build();
    }

    public ResearchSummaryDTO toDTO(ResearchSummary researchSummary, AnswerCriteriaDTO answerCriteriaDTO) {
        return ResearchSummaryDTO.builder()
                .id(researchSummary.getId())
                .title(researchSummary.getTitle())
                .criteria(answerCriteriaDTO)
                .questions(researchSummary.getQuestions().stream()
                        .map(this::toDTO)
                        .collect(toList()))
                .build();
    }

    private QuestionSummaryDTO toDTO(QuestionSummary questionSummary) {
        return QuestionSummaryDTO.builder()
                .id(questionSummary.getId())
                .description(questionSummary.getDescription())
                .options(questionSummary.getOptions().stream()
                        .map(this::toDTO)
                        .collect(toList()))
                .build();
    }

    private OptionSummaryDTO toDTO(OptionSummary optionSummary) {
        return OptionSummaryDTO.builder()
                .id(optionSummary.getId())
                .sequence(optionSummary.getSequence())
                .description(optionSummary.getDescription())
                .amount(optionSummary.getAmount())
                .build();
    }
}
