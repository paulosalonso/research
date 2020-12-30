package com.github.paulosalonso.research.adapter.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OptionInputDTO {

    @NotBlank
    private String description;

}
