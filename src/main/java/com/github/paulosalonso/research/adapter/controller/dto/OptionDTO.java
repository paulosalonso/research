package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel("Option")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OptionDTO {

    private String id;
    private Integer sequence;
    private String description;
}
