package com.github.paulosalonso.research.adapter.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchInputDTO {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private OffsetDateTime startsOn;

    private OffsetDateTime endsOn;
}
