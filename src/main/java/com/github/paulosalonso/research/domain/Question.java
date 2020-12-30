package com.github.paulosalonso.research.domain;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Question {

    private UUID id;

    private Integer sequence;

    @NonNull
    private final String description;

    @NonNull
    private final Boolean multiSelect;

    private Set<Option> options;

}
