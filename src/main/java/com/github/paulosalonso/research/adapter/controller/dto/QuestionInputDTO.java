package com.github.paulosalonso.research.adapter.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class QuestionInputDTO {

    @NotBlank
    private String description;

    @NotNull
    private Boolean multiSelect;
}
