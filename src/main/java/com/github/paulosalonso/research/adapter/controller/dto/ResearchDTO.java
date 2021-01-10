package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApiModel("Research")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchDTO {

    private UUID id;
    private String title;
    private String description;
    private OffsetDateTime startsOn;
    private OffsetDateTime endsOn;
    private List<QuestionDTO> questions;
}
