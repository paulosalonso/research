package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel("QuestionCriteria")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class QuestionCriteriaDTO {
    private String description;
    private Boolean multiSelect;
}
