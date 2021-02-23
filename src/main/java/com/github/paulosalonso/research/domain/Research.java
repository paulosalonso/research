package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Research {

    private UUID id;

    private final String tenant;

    @NonNull
    private final String title;

    private final String description;

    @NonNull
    private final OffsetDateTime startsOn;

    private final OffsetDateTime endsOn;
    private Set<Question> questions;

}
