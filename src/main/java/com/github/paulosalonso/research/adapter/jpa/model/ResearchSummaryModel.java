package com.github.paulosalonso.research.adapter.jpa.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchSummaryModel {
    public String questionId;
    public String optionId;
    public Long amount;
}
