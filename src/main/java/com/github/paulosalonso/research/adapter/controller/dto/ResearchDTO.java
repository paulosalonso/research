package com.github.paulosalonso.research.adapter.controller.dto;

import lombok.*;

import java.time.OffsetDateTime;

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
