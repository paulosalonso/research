package com.github.paulosalonso.research.application.dto;

import lombok.*;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchCriteriaDTO {

    private String title;
    private String description;
    private OffsetDateTime startsOnFrom;
    private OffsetDateTime startsOnTo;
    private OffsetDateTime endsOnFrom;
    private OffsetDateTime endsOnTo;
}
