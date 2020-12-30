package com.github.paulosalonso.research.adapter.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class QuestionInputDTO {

    @NotNull
    private String description;

    @NotNull
    private Boolean multiSelect;
}
