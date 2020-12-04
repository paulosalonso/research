package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Research {

    private UUID id;

    @NonNull
    private final String description;

    @NonNull
    private final LocalDateTime startsOn;

    private final LocalDateTime endsOn;
    private Set<Question> questions;

}
