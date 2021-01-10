package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ResearchRead {

    private final ResearchPort researchPort;

    public Research read(UUID id, boolean includeQuestions) {
        if (includeQuestions) {
            return researchPort.readFetchingQuestions(id);
        }

        return researchPort.read(id);
    }

    public List<Research> search(ResearchCriteria criteria) {
        return researchPort.search(criteria);
    }
}
