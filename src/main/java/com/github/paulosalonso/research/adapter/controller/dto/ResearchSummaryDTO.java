package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;
import java.util.UUID;

@ApiModel("ResearchSummary")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchSummaryDTO {
    private UUID id;
    private String title;
    private AnswerCriteriaDTO criteria;

    @Singular
    private List<QuestionSummaryDTO> questions;

    @ApiModel("QuestionSummary")
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

    @ApiModel("OptionSummary")
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class OptionSummaryDTO {
        private UUID id;
        private Integer sequence;
        private String description;
        private Long amount;
    }
}
