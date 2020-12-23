package com.github.paulosalonso.research.application.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AnswerDTO {
    private OffsetDateTime date;
    private UUID researchId;
    private UUID questionId;
    private UUID optionId;
}
