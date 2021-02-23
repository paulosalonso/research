package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Builder
public class ResearchCriteria {

    private final String tenant;
    private final String title;
    private final String description;
    private final OffsetDateTime startsOnFrom;
    private final OffsetDateTime startsOnTo;
    private final OffsetDateTime endsOnFrom;
    private final OffsetDateTime endsOnTo;
}
