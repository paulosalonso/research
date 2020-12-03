package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResearchUpdate {

    private final ResearchPort researchPort;

    public Research update(Research research) {
        return researchPort.update(research);
    }
}
