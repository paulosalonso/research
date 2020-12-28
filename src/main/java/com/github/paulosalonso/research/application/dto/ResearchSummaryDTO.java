package com.github.paulosalonso.research.application.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchSummaryDTO {
    private UUID id;
    private String title;
    private AnswerCriteriaInputDTO criteria;

    @Singular
    private List<QuestionSummaryDTO> questions;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class QuestionSummaryDTO {
        private UUID id;
        private String description;
        private List<OptionSummaryDTO> options;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class OptionSummaryDTO {
        private UUID id;
        private String description;
        private Long amount;
    }
}
