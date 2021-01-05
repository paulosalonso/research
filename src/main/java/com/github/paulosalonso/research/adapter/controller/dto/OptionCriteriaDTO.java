package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel("OptionCriteria")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OptionCriteriaDTO {

    private String description;
}
