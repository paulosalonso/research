package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@ApiModel("ResearchAnswerInput")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchAnswerInputDTO {

    @Singular
    @NotEmpty
    private List<QuestionAnswerInputDTO> answers;

    @ApiModel("QuestionAnswerInput")
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
