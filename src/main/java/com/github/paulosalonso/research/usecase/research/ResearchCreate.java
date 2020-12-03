package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResearchCreate {

    private final ResearchPort researchPort;

    public Research create(Research research) {
        return researchPort.create(research);
    }
}
