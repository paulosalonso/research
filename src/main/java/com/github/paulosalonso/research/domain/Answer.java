package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Answer {
    private OffsetDateTime date;
    private UUID researchId;
    private UUID questionId;
    private UUID optionId;
}
