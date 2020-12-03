package com.github.paulosalonso.research.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
public class Research {

    private UUID id;
    private final String description;
    private final LocalDateTime startsOn;
    private final LocalDateTime endsOn;
    private Set<Question> questions;

}
