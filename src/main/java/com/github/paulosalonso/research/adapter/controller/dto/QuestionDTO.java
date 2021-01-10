package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;
import java.util.UUID;

@ApiModel("Question")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionDTO {

    private UUID id;
    private Integer sequence;
    private String description;
    private Boolean multiSelect;
    private List<OptionDTO> options;

}
