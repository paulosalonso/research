package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
public class AnswerCriteria {
    private OffsetDateTime dateFrom;
    private OffsetDateTime dateTo;
    private UUID researchId;
    private UUID questionId;
    private UUID optionId;
}
