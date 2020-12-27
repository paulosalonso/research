package com.github.paulosalonso.research.application.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchAnswerInputDTO {

    @Singular
    @NotEmpty
    private List<QuestionAnswerInputDTO> answers;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class QuestionAnswerInputDTO {
        private UUID questionId;
        private UUID optionId;
    }
}
