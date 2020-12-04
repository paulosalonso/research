package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ResearchCreate {

    private final ResearchPort researchPort;

    public Research create(Research research) {
        research = research.toBuilder()
                .id(UUID.randomUUID())
                .build();

        return researchPort.create(research);
    }
}
