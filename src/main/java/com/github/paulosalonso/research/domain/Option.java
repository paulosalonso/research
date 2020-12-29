package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Option {

    private UUID id;

    private Integer sequence;

    @NonNull
    private final String description;

}
