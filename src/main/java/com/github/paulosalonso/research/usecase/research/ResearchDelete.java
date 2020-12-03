package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ResearchDelete {

    private final ResearchPort researchPort;

    public void delete(UUID id) {
        researchPort.delete(id);
    }
}
