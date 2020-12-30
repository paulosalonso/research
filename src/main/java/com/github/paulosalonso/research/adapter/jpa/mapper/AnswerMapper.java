package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.*;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.ResearchSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.OptionSummary;
import com.github.paulosalonso.research.domain.ResearchSummary.QuestionSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyList;
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
                .questions(fillQuestions(research, groupedByQuestionSummary))
                .build();
    }

    private List<QuestionSummary> fillQuestions(ResearchEntity research,
                                            Map<QuestionEntity, List<ResearchSummaryModel>> groupedByQuestionSummary) {

        return research.getQuestions().stream()
                .map(question -> QuestionSummary.builder()
                        .id(UUID.fromString(question.getId()))
                        .sequence(question.getSequence())
                        .description(question.getDescription())
                        .options(fillOptions(question, groupedByQuestionSummary.getOrDefault(question, emptyList())))
                        .build())
                .collect(toList());
    }

    private List<OptionSummary> fillOptions(QuestionEntity question, List<ResearchSummaryModel> questionSummary) {
        return question.getOptions().stream()
                .map(option -> OptionSummary.builder()
                        .id(UUID.fromString(option.getId()))
                        .sequence(option.getSequence())
                        .description(option.getDescription())
                        .amount(questionSummary.stream()
                                .filter(optionSummary -> optionSummary.getOption().equals(option))
                                .findFirst()
                                .map(ResearchSummaryModel::getAmount)
                                .orElse(0L))
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
