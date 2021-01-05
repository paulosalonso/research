package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;

@ApiModel("OptionInput")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OptionInputDTO {

    @NotBlank
    private String description;

}
