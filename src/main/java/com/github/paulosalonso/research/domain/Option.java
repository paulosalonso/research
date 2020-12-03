package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
public class Option {

    private UUID id;
    private final String description;

}
