package com.github.paulosalonso.research.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ResearchCriteria {

    private final String description;
}
