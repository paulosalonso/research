package com.github.paulosalonso.research.application.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResearchCriteriaDTO {

    private String title;
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startsOnFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startsOnTo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endsOnFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endsOnTo;
}
