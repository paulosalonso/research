package com.github.paulosalonso.research.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchSummary {
    private UUID id;
    private String title;
    private List<QuestionSummary> questions;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class QuestionSummary {
        private UUID id;
        private Integer sequence;
        private String description;
        private List<OptionSummary> options;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class OptionSummary {
        private UUID id;
        private Integer sequence;
        private String description;
        private Long amount;
    }
}
