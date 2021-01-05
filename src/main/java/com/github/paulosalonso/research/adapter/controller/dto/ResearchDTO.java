package com.github.paulosalonso.research.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.OffsetDateTime;

@ApiModel("Research")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchDTO {

    private String id;
    private String title;
    private String description;
    private OffsetDateTime startsOn;
    private OffsetDateTime endsOn;
}
