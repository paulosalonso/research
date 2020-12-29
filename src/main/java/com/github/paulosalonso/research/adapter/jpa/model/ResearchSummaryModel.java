package com.github.paulosalonso.research.adapter.jpa.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchSummaryModel {
    public QuestionEntity question;
    public OptionEntity option;
    public Long amount;
}
