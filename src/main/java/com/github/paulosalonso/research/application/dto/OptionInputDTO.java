package com.github.paulosalonso.research.application.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OptionInputDTO {

    @NotNull
    private String description;

}
