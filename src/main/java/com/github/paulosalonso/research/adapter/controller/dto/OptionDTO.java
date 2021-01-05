package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.UUID;

@ApiModel("Option")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OptionDTO {

    private UUID id;
    private Integer sequence;
    private String description;
}
