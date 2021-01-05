package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel("Question")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionDTO {

    private String id;
    private Integer sequence;
    private String description;
    private Boolean multiSelect;

}
