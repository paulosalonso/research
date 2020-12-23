package com.github.paulosalonso.research.application.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AnswerInputDTO {
    private UUID questionId;
    private UUID optionId;
}
