package com.github.paulosalonso.research.adapter.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.*;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.ResearchSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.OptionSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.QuestionSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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

    public ResearchSummary toDomain(ResearchEntity research, List<ResearchSummaryModel> researchSummary) {
        var groupedByQuestionSummary = researchSummary.stream()
                .collect(groupingBy(ResearchSummaryModel::getQuestion, toList()));

        return ResearchSummary.builder()
                .id(UUID.fromString(research.getId()))
                .title(research.getTitle())
                .questions(fillQuestionSummary(groupedByQuestionSummary))
                .build();
    }

    private List<QuestionSummary> fillQuestionSummary(Map<QuestionEntity, List<ResearchSummaryModel>> groupedByQuestionSummary) {
        return groupedByQuestionSummary.keySet().stream()
                .map(question -> QuestionSummary.builder()
                        .id(UUID.fromString(question.getId()))
                        .description(question.getDescription())
                        .options(fillOptionSummary(groupedByQuestionSummary.get(question)))
                        .build())
                .collect(toList());
    }

    private List<OptionSummary> fillOptionSummary(List<ResearchSummaryModel> questionSummary) {
        return questionSummary.stream()
                .map(summaryEntry -> OptionSummary.builder()
                        .id(UUID.fromString(summaryEntry.getOption().getId()))
                        .sequence(summaryEntry.getOption().getSequence())
                        .description(summaryEntry.getOption().getDescription())
                        .amount(summaryEntry.getAmount())
                        .build())
                .collect(toList());
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
